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
		tasks.add(task);
	}

	public void startTask() {
		while(!tasks.isEmpty() && !threadPool.isEmpty())
		{
			threadPool.runTask(tasks.poll());
		}
	}
}
