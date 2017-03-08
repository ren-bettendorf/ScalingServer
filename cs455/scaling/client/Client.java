package cs455.scaling.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;

import cs455.scaling.util.HashingFunction;
import cs455.scaling.util.ClientMessageTracker;

public class Client {
	private LinkedList<String> hashcodes;
	private Selector selector;
	private final int bufferSize = 8000;
	private final int messageRate;
	private ClientMessageTracker messageTracker;

	public Client(String serverHost, int port, int messageRate) throws IOException {
		this.hashcodes = new LinkedList<String>();
		this.messageRate = messageRate;
		this.messageTracker = new ClientMessageTracker();
		startClient(serverHost, port);
	}

	public static void main(String[] args) {
		if(args.length != 3) {
                        System.out.println("Arguments not correct server-address server-port message-rate");
                        System.exit(0);
                }
		String serverHost = args[0];
                int serverPort = -1;
                int messageRate = -1;

                try {
                        serverPort = Integer.parseInt(args[1]);
                        messageRate = Integer.parseInt(args[2]);
                } catch(NumberFormatException nfe) {
                        System.out.println("Something went wrong with parsing port and message rate");
                        System.exit(0);
                }
                try {
                        System.out.println("Starting client: " + InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException uhe) {
                        System.out.println("Something went wrong with localhost");
                }
                try{
                        new Client(serverHost, serverPort, messageRate);
                }catch (IOException ioe) {
                        System.out.println("IOException: Exiting program");
                        ioe.printStackTrace();
                        System.exit(0);
                }
	}

	public void startClient(String serverHost, int serverPort) throws IOException {
		// Open selector for incoming connections
		this.selector = Selector.open();

		// Open SocketChannel for incoming connections
		SocketChannel socketChannel = SocketChannel.open();
		// Make non blocking
		socketChannel.configureBlocking(false);
		// Connect to the server
		socketChannel.connect(new InetSocketAddress(serverHost, serverPort));
		// Set channel ready for accepting connections
		socketChannel.register(selector, SelectionKey.OP_CONNECT);

		while(true) {
			// Select set of keys ready
			selector.select();
			
			Iterator keys = selector.selectedKeys().iterator();
			// Prepare bytebuffer always ready for 40 bytes of data
			ByteBuffer buffer = ByteBuffer.allocate(40);
			
			// Iterate through keys that have a new task
			while(keys.hasNext()) {
				SelectionKey key = (SelectionKey) keys.next();
				keys.remove();
				
				synchronized(key) {
					// Connect to server if haven't already otherwise read data if available
					if(key.isConnectable()){
						this.connect(key);
					}else if(key.isReadable()) {
						this.read(key, buffer);
					}
				}
			}
		}
	}
	
	private void (SelectionKey key) {
		SocketChannel channel = (SocketChannel)key.channel();
		// Wait for channel to finish connecting
		channel.finishConnect();
		// Start thread for displaying tracker information
		startMessageTrackerThread();
		// Start thread for sending data to server
		startSenderThread(key);
		// Set key ready for reading data
		key.interestOps(SelectionKey.OP_READ);
		System.out.println("Starting SenderThread...");
	}
	
	private void read(SelectionKey key, ByteBuffer buffer) {
		System.out.println("Reading data from server...");
		buffer.clear();

		int read = 0;
		SocketChannel channel = (SocketChannel)key.channel();
		try {
			// Read entire buffer
			while(buffer.hasRemaining() && read != -1) {
				read = channel.read(buffer);
			}

			// Check for an error while reading
			if(read == -1) {
				System.out.println("Something went wrong...");
				channel.close();
				key.cancel();
				return;
			}
			byte[] data = new byte[40];
			System.arraycopy(buffer.array(), 0, data, 0, 40);
			messageTracker.incrementMessagesReceived();

			System.out.println("Removed Status: " + messageTracker.removeHashcode(new String(data)));
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			selector.wakeup();
		}
	}

	private void startMessageTrackerThread() {
		new Thread(messageTracker).start();
	}

	private void startSenderThread(SelectionKey key) {
		new Thread(new SenderThread(key, selector, messageRate, messageTracker)).start();
	}
}
