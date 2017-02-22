package cs455.scaling.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.*;
import java.nio.ByteBuffer;

import cs455.scaling.threads.*;

public class Server {

	private final int port, numberThreads;
	private final String hostAddress;
	private Selector selector;
	private ThreadPoolManager threadPoolManager;
	
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

		runServer();
	}

	public static void main(String[] args) {
		System.out.println("Yarp");
	
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
		try{
			new Server(port, numberThreads);
		}catch (IOException ioe) {
			System.out.println("IOException: Exiting program");
			ioe.printStackTrace();
			System.exit(0);
		}
	}

	public void runServer() throws IOException {
		this.selector = Selector.open();
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);

		serverSocketChannel.socket().bind(new InetSocketAddress(hostAddress, port));

		serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

	}


}
