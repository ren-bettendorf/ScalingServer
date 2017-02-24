package cs455.scaling.threads;

import java.util.LinkedList;

public class ThreadPool {

	private LinkedList<Worker> threadPool;

	public ThreadPool(int threadPoolSize) {
		threadPool = new LinkedList<Worker>();
		System.out.println("Creating ThreadPool of size " + threadPoolSize);
		for(int i = 0; i < threadPoolSize; i++) {
			System.out.println("Creating worker thread " + (i+1));
			Worker worker = new Worker(this);
			threadPool.add(worker);
			new Thread(worker).start();
		}
	}

	public void runTask(Task task) {
		synchronized(threadPool) {
			Worker worker = threadPool.poll();
			worker.addTask(task);
		}
	}

	public void addBackToPool(Worker worker) {
		synchronized(threadPool) {
			if(!threadPool.contains(worker)) {
				threadPool.add(worker);
			}
		}
	}

	public boolean isEmpty() {
		synchronized(threadPool) {
			return threadPool.isEmpty(); 
		}
	}

	public int getSize() {
		synchronized(threadPool) {
			return threadPool.size();
		}
	}
}
