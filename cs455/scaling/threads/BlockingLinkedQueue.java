package cs455.scaling.threads;

import java.util.LinkedList;

public class BlockingLinkedQueue {

	private LinkedList<Task> tasks;

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

	public synchronized boolean isEmpty() {
		return tasks.isEmpty();
	}
}
