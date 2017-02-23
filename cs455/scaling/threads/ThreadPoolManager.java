package cs455.scaling.threads;

import java.util.LinkedList;

public class ThreadPoolManager {

	private ThreadPool threadPool;
	private BlockingLinkedQueue tasks;

	public ThreadPoolManager(int threadPoolSize) {
		this.threadPool = new ThreadPool(threadPoolSize);
		this.tasks = new BlockingLinkedQueue();
	}

	public void addTask(Task task) {
		synchronized(tasks) {
			tasks.add(task);
		}
	}

}
