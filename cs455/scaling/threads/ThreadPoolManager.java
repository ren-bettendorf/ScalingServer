package cs455.scaling.threads;

import java.util.LinkedList;

public class ThreadPoolManager {

	private ThreadPool threadPool;
	private LinkedList<Task> tasks;

	public ThreadPoolManager(int threadPoolSize) {
		this.threadPool = new ThreadPool(threadPoolSize);
		this.tasks = new LinkedList<Task>();
	}

	public void addTask(Task task) {
		synchronized(tasks) {
			tasks.add(task);
		}
	}

}
