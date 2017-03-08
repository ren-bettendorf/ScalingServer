package cs455.scaling.threads;

public class Worker implements Runnable {
	
	private ThreadPool pool;
	private Task currentTask;
	private boolean taskStatus = false;
	private final Object lock = new Object();

	public Worker(ThreadPool pool) {
		this.pool = pool;
		this.currentTask = null;
	}
	
	public void addTask(Task task) {
		if(task != null) {
			synchronized(lock){
				this.currentTask = task;
			}
		}
	}

	@Override
	public void run() {
		while(true) {
			synchronized(lock) {
				if(currentTask != null) {
					// Run the task
					currentTask.startTask();
					// Will set to null or a new task
					currentTask = pool.addBackToPool(this);
				}
			}
		}
	}

}
