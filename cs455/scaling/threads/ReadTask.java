package cs455.scaling.threads;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ReadTask implements Task, TaskType {

	private SelectionKey key;
	private SocketChannel channel;
	private int type, bufferSize;

	public ReadTask(SelectionKey key, SocketChannel channel, int bufferSize) {
		this.key = key;
		this.channel = channel;
		this.type = TaskType.READ_TASK;
		this.bufferSize = bufferSize;
	}

	@Override
	public void run() {
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		int read = 0;

		try {
			while(buffer.hasRemaining() && read != -1) {
				read = channel.read(buffer);
			}
		} catch(IOException ioe) {
			buffer.clear();
			return;
		}
		byte[] data = new byte[bufferSize];
		buffer.get(data);

		buffer.clear();
		key.interestOps(SelectionKey.OP_WRITE);
	}

	@Override
	public int getTaskType() {
		return type;
	}
}
