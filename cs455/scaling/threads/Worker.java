package cs455.scaling.threads;

public class Worker implements Runnable {
	
	private ThreadPool pool;

	public Worker(ThreadPool pool) {
		this.pool = pool;
	}

	@Override
	public void run() {
		

		while(true) {
			pool.addBackToPool(this);
		}
	}

}
