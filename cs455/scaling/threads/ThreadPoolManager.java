package cs455.scaling.threads;

import java.util.Queue;
import java.util.LinkedList;

public class ThreadPoolManager {

	private ThreadPool threadPool;
	private Queue<Task> tasks;

	public ThreadPoolManager() {
		this.tasks = new LinkedList<Task>();
	}

	public void initializeThreadPool(int threadPoolSize) {
		this.threadPool = new ThreadPool(threadPoolSize, this);
	}

	public void addTask(Task task) {
		synchronized(tasks) {
			tasks.add(task);
		}
		if(!threadPool.isEmpty()) {
			threadPool.giveWorkerTask(removeTask());
		}
	}

	public boolean checkForMoreTasks() {
		synchronized(tasks) {
			return tasks.isEmpty();
		}
	}

	public Task removeTask() {
		synchronized(tasks) {
			return tasks.poll();
		}
	}
}
