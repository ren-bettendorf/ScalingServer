package cs455.scaling.threads;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ReadTask implements Task{

	private SelectionKey key;
	private SocketChannel channel;
	private int bufferSize;

	public ReadTask(SelectionKey key, SocketChannel channel, int bufferSize) {
		this.key = key;
		this.channel = channel;
		this.bufferSize = bufferSize;
	}

	@Override
	public void startTask() {
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
}
