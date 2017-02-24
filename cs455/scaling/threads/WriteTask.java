package cs455.scaling.threads;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class WriteTask implements Task, TaskType {

	private SelectionKey key;
	private SocketChannel channel;
	private int type, bufferSize;

	public WriteTask(SelectionKey key, SocketChannel channel, int bufferSize) {
		this.key = key;
		this.channel = channel;
		this.type = TaskType.WRITE_TASK;
		this.bufferSize = bufferSize;
	}
	
	@Override
	public void run() {  
		

	}

	@Override
	public int getTaskType() {
		return this.type;
	}
}
