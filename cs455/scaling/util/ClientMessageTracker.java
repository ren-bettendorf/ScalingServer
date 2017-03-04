package cs455.scaling.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientMessageTracker implements Runnable {

	// Will use to message every 10seconds
	private final int messageRate;
	private int messageSent, messageReceived;
	private final Object lock = new Object();
	private final DateTimeFormatter formatter;
	
	public ClientMessageTracker() {
		this.messageSent = 0;
		this.messageReceived = 0;
		this.messageRate = 10;
		formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm:ss");
	}

	public void incrementMessagesSent() {
		synchronized(lock) {
			messageSent++;
		}
	}

	public void incrementMessagesReceived() {
		synchronized(lock) {
			messageReceived++;
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
					System.out.println("[" + messageTime.format(formatter) + "] Total Sent Count: " + messageSent + ", Total Received Count: " + messageReceived );
					messageSent = 0;
					messageReceived = 0;
				}
			}
		}
	}
}
