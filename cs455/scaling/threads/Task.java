package cs455.scaling.threads;

public interface Task extends Runnable {

	@Override
	public void run();

	public int getTaskType(); 
}
