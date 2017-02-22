package cs455.scaling.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.LinkedList;
import java.util.Iterator;

public class Client {
	private LinkedList<String> hashcodes;
	private Selector selector;
	private int buffSize = 8000;

	public Client(String serverHost, int port) throws IOException {
		this.hashcodes = new LinkedList<String>();
		startClient(serverHost, port);
	}

	public static void main(String[] args) {
		System.out.println("NARP");
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
				}else if(key.isReadable()) {
					ByteBuffer buffer = ByteBuffer.allocate(buffSize);
				}
			}
		}
	}

	private void connect(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		channel.finishConnect();
		key.interestOps(SelectionKey.OP_WRITE);
	}
}
