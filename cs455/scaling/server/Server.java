package cs455.scaling.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.util.Iterator;

import cs455.scaling.threads.*;

public class Server {

	private final int port, numberThreads;
	private final String hostAddress;
	private Selector selector;
	private ThreadPoolManager threadPoolManager;
	private int buffSize = 8192;

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
		this.threadPoolManager = new ThreadPoolManager(numberThreads);
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
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);

		serverSocketChannel.socket().bind(new InetSocketAddress(hostAddress, port));

		serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		
		while(true) {
			this.selector.select();
			
			Iterator keys = this.selector.selectedKeys().iterator();

			while(keys.hasNext()) {
				SelectionKey key = (SelectionKey) keys.next();
				if(key.isValid()) {
					if(key.isAcceptable()) {
						this.accept(key);
					}else if(key.isReadable()) {
						this.read(key);
					}//else if(key.isWritable()) {
					//	this.write(key);
					//}
				}
			}
		}
	}

	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel servSocket = (ServerSocketChannel) key.channel();
		SocketChannel channel = servSocket.accept();

		System.out.println("Accepting incoming connection..");
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_READ);
	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();

		ByteBuffer buffer = ByteBuffer.allocate(buffSize);
		int read = 0;

		try {
			while(buffer.hasRemaining() && read != -1) {
				read = channel.read(buffer);
			}
		} catch(IOException ioe) {
			// Disconnect the key!!! TODO
			return;
		}
		key.interestOps(SelectionKey.OP_WRITE);
	}

	private void write(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		// DATA NEEDS TO BE CREATED HERE
		byte[] data = new byte[5];
		
		ByteBuffer buffer = ByteBuffer.wrap(data);
		channel.write(buffer);
		key.interestOps(SelectionKey.OP_READ);
	}
}
