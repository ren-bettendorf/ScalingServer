package cs455.scaling.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {

	private final int port, numberThreads;
	private final String hostAddress;

	public Server(int port, int numberThreads) {
		this.port = port;
		this.numberThreads = numberThreads;
		String tempHost = "";
		try {
			tempHost = InetAddress.getLocalHost().getHostAddress();
		} catch(UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		this.hostAddress = tempHost;
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

		new Server(port, numberThreads);
	}




}
