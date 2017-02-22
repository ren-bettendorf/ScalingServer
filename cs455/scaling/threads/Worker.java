package cs455.scaling.threads;

public class Worker implements Runnable {
	
	private ThreadPoolManager manager;

	public Worker(ThreadPoolManager threadPoolManager) {
		this.manager = threadPoolManager;
	}

	@Override
	public void run() {
		


		manager.addBackToPool(this);
	}

}
