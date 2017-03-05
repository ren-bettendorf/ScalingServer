package cs455.scaling.util;

import java.util.List;
import java.util.LinkedList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientMessageTracker implements Runnable {

	// Will use to message every 10seconds
	private final int messageRate = 10;
	private int messageSent, messageReceived;
	private final Object lock = new Object();
	private final DateTimeFormatter formatter;
	private List<String> hashcodes;

	public ClientMessageTracker() {
		this.hashcodes = new LinkedList<String>();
		this.messageSent = 0;
		this.messageReceived = 0;
		formatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss");
	}

	public void addHashcode(String hashcode) {
		synchronized(hashcodes) {
			hashcodes.add(hashcode);
		}
	}

	public boolean removeHashcode(String hashcode) {
		synchronized(hashcodes) {
			if(hashcodes.contains(hashcode)) {
				hashcodes.remove(hashcode);
				return true;
			}
		}
		return false;
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
