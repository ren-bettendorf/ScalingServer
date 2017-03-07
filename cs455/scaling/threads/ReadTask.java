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
		State state = (State) key.attachment();
		state.setReadingState(true);
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		int read = 0;
		buffer.clear();
		synchronized(key) {
			try {

				while(buffer.hasRemaining() && read != -1) {
            				read = channel.read(buffer);
				}

            			if (read == -1) {
                			System.out.println("Connection closed by client: " + channel.socket().getRemoteSocketAddress());
					messageTracker.decrementActiveConnections(channel.socket().getRemoteSocketAddress().toString());
                			channel.close();
                			key.cancel();
                			return;
            			}
				byte[] data = new byte[bufferSize];

				System.arraycopy(buffer.array(), 0, data, 0, bufferSize);
				System.out.println("READING: Data Size: "+ data.length);
				messageTracker.incrementMessageThroughput();
				key.interestOps(SelectionKey.OP_WRITE);

				String hashcode = HashingFunction.getInstance().SHA1FromBytes(data);
				System.out.println("Attaching: " + hashcode);
	            		//state.setData(hashcode);
				threadPoolManager.addTask(new WriteTask(key, selector, hashcode, messageTracker));
        		} catch (IOException ioe ) {
				ioe.printStackTrace();
			} catch ( NoSuchAlgorithmException nsae) {
            			nsae.printStackTrace();
        		}finally {
				state.setReadingState(false);
				selector.wakeup();
			}
		}
	}
}
