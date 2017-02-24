package cs455.scaling.threads;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ReadTask implements Task, TaskType {

	private SelectionKey key;
	private SocketChannel channel;
	private int type;

	public ReadTask(SelectionKey key, SocketChannel channel) {
		this.key = key;
		this.channel = channel;
		this.type = TaskType.READ_TASK;
	}

	@Override
	public void run() {

	}

	@Override
	public int getTaskType() {
		return type;
	}
}
