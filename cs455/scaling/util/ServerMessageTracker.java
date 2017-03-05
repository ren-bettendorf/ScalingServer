package cs455.scaling.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerMessageTracker implements Runnable {
	// Will use to message every 5 seconds
	private final int messageRate = 5;
	private int messageThroughput, activeConnections;
	private final Object lock = new Object();
	private final DateTimeFormatter formatter;

	public ServerMessageTracker() {
		this.messageThroughput = 0;
		this.activeConnections = 0;
		formatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss");
	}

	public void incrementMessageThroughput() {
		synchronized(lock) {
			messageThroughput++;
		}
	}

	public void incrementActiveConnections() {
		synchronized(lock) {
			activeConnections++;
		}
	}

	public void decrementActiveConnections() {
		synchronized(lock) {
			activeConnections--;
		}
	}

	@Override
	public void run() {
		LocalDateTime messageTime = LocalDateTime.now().plusSeconds(messageRate);
		while(true) {
			LocalDateTime current = LocalDateTime.now();
			if(current.isAfter(messageTime)) {
				messageTime = current.plusSeconds(messageRate);
				synchronized(lock) {
					System.out.println("[" + messageTime.format(formatter) + "] Total Sent Count: " + messageThroughput + ", Active Clients: " + activeConnections );
					messageThroughput = 0;
				}
			}
		}
	}
}
