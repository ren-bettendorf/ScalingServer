/*package cs455.scaling.threads;

import java.util.Queue;
import java.util.LinkedList;

public class BlockingLinkedQueue {

	private Queue<Task> tasks;

	public BlockingLinkedQueue() {
		tasks = new LinkedList<Task>();
	}

	public synchronized void add(Task task) {
		tasks.addLast(task);
	}

	public synchronized Task poll() {
		Task output = null;
		if(!tasks.isEmpty()) {
			output = tasks.poll();
		}
		return output;
	}
	
	public synchronized int getSize() {
		return tasks.size();
	}

	public synchronized boolean isEmpty() {
		return tasks.isEmpty();
	}
}
*/
