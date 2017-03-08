package cs455.scaling.threads;

import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;

import cs455.scaling.util.HashingFunction;
import cs455.scaling.util.State;
import cs455.scaling.util.ServerMessageTracker;

public class ReadTask extends Task{

	private SelectionKey key;
	private SocketChannel channel;
	private Selector selector;
	private ThreadPoolManager threadPoolManager;
	private ServerMessageTracker messageTracker;

	public ReadTask(SelectionKey key, Selector selector, ThreadPoolManager threadPoolManager, ServerMessageTracker messageTracker) {
		this.key = key;
		this.channel = (SocketChannel) key.channel();
		this.selector = selector;
		this.threadPoolManager = threadPoolManager;
		this.messageTracker = messageTracker;
	}

	@Override
	public void startTask() {
		// Get buffer ready for reading
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		int read = 0;
		buffer.clear();

		try {	
			// Read entire buffer
			while(buffer.hasRemaining() && read != -1) {
						read = channel.read(buffer);
			}
			// Check for an error while reading
			if (read == -1) {
				System.out.println("Connection closed by client: " + channel.socket().getRemoteSocketAddress());
				messageTracker.decrementActiveConnections(channel.socket().getRemoteSocketAddress().toString());
				channel.close();
				key.cancel();
				return;
			}
			
			// Copy data from buffer to an array
			byte[] data = new byte[bufferSize];
			System.arraycopy(buffer.array(), 0, data, 0, bufferSize);
			// Increment throughput counter
			messageTracker.incrementMessageThroughput();
			// Generate SHA1 hash from data from buffer
			String hashcode = HashingFunction.getInstance().SHA1FromBytes(data);
			System.out.println("Attaching: " + hashcode);
			// Create and add a writing task to the thread pool
			threadPoolManager.addTask(new WriteTask(key, selector, hashcode, messageTracker));
			
		} catch (IOException ioe ) {
			return;
		} catch ( NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
	}
}
