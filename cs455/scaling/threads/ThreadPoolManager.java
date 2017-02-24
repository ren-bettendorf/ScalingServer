package cs455.scaling.threads;

import java.util.LinkedList;

public class ThreadPoolManager {

	private ThreadPool threadPool;
	private BlockingLinkedQueue tasks;

	public ThreadPoolManager(int threadPoolSize) {
		//this.threadPool = new ThreadPool(threadPoolSize);
		this.tasks = new BlockingLinkedQueue();
	}
	
	public void initializeThreadPool(int threadPoolSize) {
		this.threadPool = new ThreadPool(threadPoolSize);
	}

	public void addTask(Task task) {
		tasks.add(task);
	}

	public void startTask() {
		while(true) {
			while(!tasks.isEmpty() && !threadPool.isEmpty())
			{
				System.out.println(tasks.getSize() + " " + threadPool.getSize());
				threadPool.runTask(tasks.poll());
			}
		}
	}
}
