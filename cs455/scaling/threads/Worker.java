package cs455.scaling.threads;

public class Worker implements Runnable {
	
	private ThreadPool pool;
	private Task currentTask;
	private boolean taskStatus = false;
	private final Object lock = new Object();

	public Worker(ThreadPool pool) {
		this.pool = pool;
	}
	
	public void addTask(Task task) {
		synchronized(lock){
			this.currentTask = task;
			taskStatus = true;
		}
	}

	@Override
	public void run() {
		while(true) {
			synchronized(lock) {
				if(taskStatus) {
					currentTask.startTask();
					taskStatus = false;
					pool.addBackToPool(this);
				}
			}
		}
	}

}
