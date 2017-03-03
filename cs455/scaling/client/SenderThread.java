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
import java.security.NoSuchAlgorithmException;

public class SenderThread implements Runnable {
	
	private final int messageRate;
	private SelectionKey key;
	private Selector selector;

	public SenderThread(SelectionKey key, Selector selector, int messageDivisor) {
		this.messageRate = 1000 / messageDivisor;
		this.key = key;
		this.selector = selector;
	}

	@Override
	public void run() {
		SocketChannel channel = (SocketChannel) key.channel();
		//while(true) {
			byte[] dataToBeWritten = createRandomData();
			ByteBuffer buffer = ByteBuffer.allocate(8000);
			buffer.flip();
			synchronized(key) {
			try {
				channel.write(buffer.wrap(dataToBeWritten));
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}finally {
				System.out.println("Interested in reading...");
				key.interestOps(SelectionKey.OP_READ);
				selector.wakeup();
			}
			}
			try {
				System.out.println("Sleeping for " + messageRate);
				Thread.sleep( messageRate );
			} catch(Exception e) {
				e.printStackTrace();
			}
		//}
	}

	private byte[] createRandomData() {
		byte[] data = new byte[8000];

		(new Random()).nextBytes(data);

		return data;
	}
}
