package cs455.scaling.threads;

import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cs455.scaling.util.HashingFunction;
import cs455.scaling.util.State;

public class ReadTask extends Task{

	private SelectionKey key;
	private SocketChannel channel;
	private ThreadPoolManager threadPoolManager;
	
	public ReadTask(SelectionKey key, SocketChannel channel, ThreadPoolManager threadPoolManager) {
		this.key = key;
		this.channel = channel;
		this.threadPoolManager = threadPoolManager;
	}

	@Override
	public void startTask() {
		State state = (State) key.attachment();
		state.setReadingState(true);
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		int read = 0;
		buffer.clear();

		try {
			System.out.println("Reading data...");
			while(buffer.hasRemaining() && read != -1) {
            			read = channel.read(buffer);
			}

            		if (read == -1) {
                		System.out.println("Connection closed by client: " + channel.socket().getRemoteSocketAddress());
                		channel.close();
                		key.cancel();
                		return;
            		}
			byte[] data = new byte[bufferSize];
			System.arraycopy(buffer.array(), 0, data, 0, bufferSize);

			key.interestOps(SelectionKey.OP_WRITE);
	            	WriteTask writeTask = new WriteTask(key, channel, HashingFunction.getInstance().SHA1FromBytes(data));
			System.out.println(HashingFunction.getInstance().SHA1FromBytes(data));
            		threadPoolManager.addTask(writeTask);
        	} catch (IOException ioe ) {
			ioe.printStackTrace();
		} catch ( NoSuchAlgorithmException nsae) {
            		nsae.printStackTrace();
        	}finally {
			state.setReadingState(false);
		}
	}
}
