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
import cs455.scaling.util.MessageTracker;

public class Server {

	private final int port, numberThreads;
	private final String hostAddress;
	private Selector selector;
	private ThreadPoolManager threadPoolManager;
	private ServerSocketChannel serverSocketChannel;
	
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

	public void startServer() throws IOException {
		this.selector = Selector.open();
		
		this.serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(hostAddress, port));
		serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		
		while(true) {
			this.selector.select();

			Iterator keys = this.selector.selectedKeys().iterator();

			while(keys.hasNext()) {
				SelectionKey key = (SelectionKey) keys.next();
				keys.remove();
				synchronized(key) {
				if(!key.isValid()) {
					continue;
				}
				if(key.isAcceptable()) {
					this.accept(key);
				}else if(key.isReadable()) {
					this.read(key);
				}
				}
			}
		}
	}

	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel servSocket = (ServerSocketChannel) key.channel();
		SocketChannel channel = servSocket.accept();

		System.out.println("Accepting incoming connection..");
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_READ, new State());
	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		State state = (State) key.attachment();
		if(!state.getReadingState()) {
			threadPoolManager.addTask(new ReadTask(key, selector, threadPoolManager));
		}
	}
}
