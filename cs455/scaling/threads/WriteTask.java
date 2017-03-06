package cs455.scaling.threads;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.ByteBuffer;

import cs455.scaling.util.State;
import cs455.scaling.util.ServerMessageTracker;

public class WriteTask extends Task{

	private SelectionKey key;
	private SocketChannel channel;
	private Selector selector;
	private ServerMessageTracker messageTracker;
	private String data;

	public WriteTask(SelectionKey key, Selector selector, String data, ServerMessageTracker messageTracker) {
		this.key = key;
		this.channel = (SocketChannel)key.channel();
		this.selector = selector;
		this.messageTracker = messageTracker;
		this.data = data;
	}

	@Override
	public void startTask() {
		State state = (State)key.attachment();
		/*String data = state.getData();
		if(data == null) {
			System.out.println("Data is null");
			return;
		}*/

		state.setWritingState(true);
		try {
	        	System.out.println("Writing[" + data.getBytes().length + "]: " + data );
			ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
        		buffer.rewind();
			synchronized(key) {
				channel.write(buffer);
            			key.interestOps(SelectionKey.OP_READ);
			}
			messageTracker.incrementMessageThroughput();
        	} catch (IOException e) {
            		e.printStackTrace();
        	} finally {
			state.setWritingState(false);
			selector.wakeup();
		}
	}
}
