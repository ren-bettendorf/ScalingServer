package cs455.scaling.threads;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class WriteTask implements Task{

	private SelectionKey key;
	private SocketChannel channel;
	private int bufferSize;

	public WriteTask(SelectionKey key, SocketChannel channel, int bufferSize) {
		this.key = key;
		this.channel = channel;
		this.bufferSize = bufferSize;
	}
	
	@Override
	public void startTask() {  
		

	}
}
