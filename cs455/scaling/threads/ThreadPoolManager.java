package cs455.scaling.threads;

import java.util.LinkedList;

public class ThreadPoolManager {

	private final int threadPoolSize;
	private LinkedList<Worker> threadPool;
	private LinkedList<Task> tasks;

	public ThreadPoolManager(int threadPoolSize) {
		this.threadPool = new LinkedList<Worker>();
		this.threadPoolSize = threadPoolSize;
		for(int i = 0; i < threadPoolSize; i++) {
			Worker worker = new Worker();
			threadPool.add(worker);
			new Thread(worker).start();
		}

		this.tasks = new LinkedList<Task>();
	}




}
