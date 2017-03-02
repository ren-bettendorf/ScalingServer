package cs455.scaling.threads;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;

public class WriteTask extends Task{

	private SelectionKey key;
	private SocketChannel channel;
	private String data;
	public WriteTask(SelectionKey key, SocketChannel channel, String data) {
		this.key = key;
		this.channel = channel;
		this.data = data;
	}
	
	@Override
	public void startTask() {  
		try {
	        	System.out.println("Writing[" + data.length + "]: " + data );
			ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        		buffer.flip();
			buffer.wrap(data.getBytes());
			channel.write(buffer);
            		//key.interestOps(SelectionKey.OP_READ);
        	} catch (IOException e) {
            		e.printStackTrace();
        	}
	}
}
