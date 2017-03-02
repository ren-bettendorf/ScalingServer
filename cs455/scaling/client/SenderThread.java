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

public class SenderThread implements runnable {
	
	private final int messageRate;
	
	public SenderThread(SelectionKey key, int messageDivisor) {
		this.messageRate = 1000 / messageDivisor;
	}
	
	@Override
	public void run() {
		SocketChannel channel = 
		while(true) {
			byte[] dataToBeWritten = createRandomData();
			System.out.println("Writing: " + new String(dataToBeWritten);
			channel.write(ByteBuffer.wrap(dataToBeWritten));
			
			try {
				Thread.sleep( messageRate );
			} catch(Exception e) { 
				e.printStackTrace(); 
			}
		}
	}
	
	private byte[] createRandomData() {
		byte[] data = new byte[bufferSize];

		(new Random()).nextBytes(data);

		return data;
	}
}