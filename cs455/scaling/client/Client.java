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

public class Client {
	private LinkedList<String> hashcodes;
	private Selector selector;
	private final int bufferSize = 8000;
	private final int messageRate;

	public Client(String serverHost, int port, int messageRate) throws IOException {
		this.hashcodes = new LinkedList<String>();
		startClient(serverHost, port);
		this.messageRate = messageRate;
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
		this.selector = Selector.open();

		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		channel.connect(new InetSocketAddress(serverHost, serverPort));
		channel.register(selector, SelectionKey.OP_CONNECT);

		while(true) {
			selector.select();

			Iterator keys = selector.selectedKeys().iterator();

			while(keys.hasNext()) {
				SelectionKey key = (SelectionKey) keys.next();
				
				if(key.isConnectable()){
					this.connect(key);
				}else if(key.isWritable()) {
					try {
						Thread.sleep(5000);
					} catch(Exception e) { e.printStackTrace(); }

					for(int i = 0; i < 10; i++) {
						byte[] dataToBeWritten = createRandomData();
						System.out.println("Writing: " + dataToBeWritten.toString());
						channel.write(ByteBuffer.wrap(dataToBeWritten));
					}
				}else if(key.isReadable()) {
					System.out.println("Reading data from server...");
					ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
					int read = 0;

					try {
						read = channel.read(buffer);

						if(read == -1) {
							System.out.println("Something went wrong...");
							channel.close();
							key.cancel();
							return;
						}
						byte[] data = null;
						if(buffer.hasArray()) {
							data = buffer.array();
						}

						buffer.clear();
						System.out.println("Arrived: " + data.toString());
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				keys.remove();
			}
		}
	}

	private void connect(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		if(channel.finishConnect()) {
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
	}

	private byte[] createRandomData() {
		byte[] data = new byte[bufferSize];

		(new Random()).nextBytes(data);

		return data;
	}
}
