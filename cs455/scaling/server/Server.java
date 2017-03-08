package cs455.scaling.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.HashMap;

import cs455.scaling.threads.*;
import cs455.scaling.util.State;
import cs455.scaling.util.ServerMessageTracker;

public class Server {

	private final int port, numberThreads;
	private final String hostAddress;
	private Selector selector;
	private ThreadPoolManager threadPoolManager;
	private ServerSocketChannel serverSocketChannel;
	private ServerMessageTracker messageTracker;

	public Server(int port, int numberThreads) throws IOException{
		this.port = port;
		this.numberThreads = numberThreads;
		String tempHost = "";
		try {
			tempHost = InetAddress.getLocalHost().getHostAddress();
		} catch(UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		this.hostAddress = tempHost;
		this.threadPoolManager = new ThreadPoolManager();
		this.threadPoolManager.initializeThreadPool(numberThreads);
		this.messageTracker = new ServerMessageTracker();

		startServer();
	}

	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Arguments not correct port number-of-threads");
			System.exit(0);
		}
		int port = -1;
		int numberThreads = -1;

		try {
			port = Integer.parseInt(args[0]);
			numberThreads = Integer.parseInt(args[1]);
		} catch(NumberFormatException nfe) {
			System.out.println("Something went wrong with parsing port and number of threads");
			System.exit(0);
		}
		try {
			System.out.println("Starting server: " + InetAddress.getLocalHost().getHostAddress() + ":"+ port + " with " + numberThreads + " threads");
		} catch (UnknownHostException uhe) {
			System.out.println("Something went wrong with localhost");
		}
		try{
			new Server(port, numberThreads);
		}catch (IOException ioe) {
			System.out.println("IOException: Exiting program");
			ioe.printStackTrace();
			System.exit(0);
		}
	}

	private void startServer() throws IOException {
		// Open selector for incoming connections
		this.selector = Selector.open();
		// Open ServerSocketChannel for incoming connections
		this.serverSocketChannel = ServerSocketChannel.open();
		// Make non blocking
		serverSocketChannel.configureBlocking(false);
		// Bind the address for connections
		serverSocketChannel.socket().bind(new InetSocketAddress(hostAddress, port));
		// Set channel ready for accepting connections
		serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		// Start thread for displaying tracker information
		new Thread(messageTracker).start();

		while(true) {
			// Select set of keys ready
			this.selector.select();

			Iterator keys = this.selector.selectedKeys().iterator();
			// Iterate through keys that have a new task
			while(keys.hasNext()) {
				SelectionKey key = (SelectionKey) keys.next();
				keys.remove();
				// If problem with the key then continue to next one
				if(!key.isValid()) {
					continue;
				}
				// Accept the connection if acceptable otherwise read if key is ready for reading
				if(key.isAcceptable()) {
					this.accept(key);
				}else if(key.isReadable()) {
					this.read(key);
				}
			}
		}
	}

	private void accept(SelectionKey key) throws IOException {
		// Accept the connection
		ServerSocketChannel servSocket = (ServerSocketChannel) key.channel();
		SocketChannel channel = servSocket.accept();

		System.out.println("Accepting incoming connection..");
		// Make non  blocking
		channel.configureBlocking(false);
		// Make key ready for reading
		channel.register(selector, SelectionKey.OP_READ);
		System.out.println("Connected: " + channel.socket().getRemoteSocketAddress().toString());
		// Increase the active connections
		messageTracker.incrementActiveConnections(channel.socket().getRemoteSocketAddress().toString());
	}

	private void read(SelectionKey key) throws IOException {
		// Close off key for reading so we can send data
		key.interestOps(SelectionKey.OP_WRITE);
		// Create and add a reading task to the thread pool
		threadPoolManager.addTask(new ReadTask(key, selector, threadPoolManager, messageTracker));
	}
}
