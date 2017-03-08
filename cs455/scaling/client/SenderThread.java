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

import cs455.scaling.util.ClientMessageTracker;
import cs455.scaling.util.HashingFunction;

public class SenderThread implements Runnable {

	private final int messageRate;
	private SelectionKey key;
	private Selector selector;
	private ClientMessageTracker messageTracker;

	public SenderThread(SelectionKey key, Selector selector, int messageDivisor, ClientMessageTracker messageTracker) {
		this.messageRate = 1000 / messageDivisor;
		this.key = key;
		this.selector = selector;
		this.messageTracker = messageTracker;
	}

	@Override
	public void run() {
		SocketChannel channel = (SocketChannel) key.channel();
		while(true) {
			// Create random data to be sent to server
			byte[] dataToBeWritten = createRandomData();
			// Increment messages sent
			messageTracker.incrementMessagesSent();
			// Add the hash from random data to be tracked from server to client message
			try {
				String data = HashingFunction.getInstance().SHA1FromBytes(dataToBeWritten);
				messageTracker.addHashcode(data);
				System.out.println("Attaching: " + data);
			} catch(NoSuchAlgorithmException nsae) {
				nsae.printStackTrace();
			}
			// Wrap the data for sending
			ByteBuffer buffer = ByteBuffer.wrap(dataToBeWritten);
			buffer.rewind();
			// Send the data
			synchronized(key) {
				try {
					channel.write(buffer);
				}catch(IOException ioe) {
					ioe.printStackTrace();
				}finally {
					key.interestOps(SelectionKey.OP_READ);
					selector.wakeup();
				}
			}
			
			// Sleep until ready to send next message
			try {
				Thread.sleep( messageRate );
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private byte[] createRandomData() {
		byte[] data = new byte[8000];

		(new Random()).nextBytes(data);

		return data;
	}
}
