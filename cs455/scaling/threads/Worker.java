package cs455.scaling.threads;

public class Worker implements Runnable {
	
	private ThreadPool pool;
	private Task currentTask;
	private boolean taskStatus = false;

	public Worker(ThreadPool pool) {
		this.pool = pool;
	}
	
	public void addTask(Task task) {
		this.currentTask = task;
		taskStatus = true;
	}

	@Override
	public void run() {
		

		while(true) {
			if(taskStatus) {
				currentTask.run();
				taskStatus = false;
			}
			pool.addBackToPool(this);
		}
	}

}
