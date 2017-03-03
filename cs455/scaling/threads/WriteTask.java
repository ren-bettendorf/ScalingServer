package cs455.scaling.threads;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.ByteBuffer;

import cs455.scaling.util.State;

public class WriteTask extends Task{

	private SelectionKey key;
	private SocketChannel channel;
	private Selector selector;

	public WriteTask(SelectionKey key, Selector selector) {
		this.key = key;
		this.channel = (SocketChannel)key.channel();
		this.selector = selector;
	}

	@Override
	public void startTask() {
		State state = (State)key.attachment();
		String data = state.getData();
		if(data == null) { return; }

		state.setWritingState(true);
		try {
	        	System.out.println("Writing[" + data.getBytes().length + "]: " + data );
			ByteBuffer buffer = ByteBuffer.allocate(40);
        		buffer.flip();
			buffer.wrap(data.getBytes());
			synchronized(key) {
			channel.write(buffer);
            		key.interestOps(SelectionKey.OP_READ);
			}
        	} catch (IOException e) {
            		e.printStackTrace();
        	} finally {
			state.setWritingState(false);
			selector.wakeup();
		}
	}
}
