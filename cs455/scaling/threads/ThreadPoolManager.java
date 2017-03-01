package cs455.scaling.threads;

import java.util.LinkedList;

public class ThreadPoolManager {

	private ThreadPool threadPool;
	private BlockingLinkedQueue tasks;

	public ThreadPoolManager() {
		this.tasks = new BlockingLinkedQueue();
	}
	
	public void initializeThreadPool(int threadPoolSize) {
		this.threadPool = new ThreadPool(threadPoolSize, this);
	}

	public void addTask(Task task) {
		System.out.println("Adding task");
		tasks.add(task);
	}

	public boolean checkForMoreTasks() {
		return tasks.isEmpty();
	}
	
	public Task removeTask() {
		return tasks.poll();
	}
}
