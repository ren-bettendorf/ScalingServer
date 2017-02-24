package cs455.scaling.threads;

import java.lang.InterruptedException;

public class DummyTask implements Task {
	
	private final int sleepTime, taskNumber;

	public DummyTask(int taskNumber, int sleepTime) {
		this.taskNumber = taskNumber;
		this.sleepTime = sleepTime;
	}


	@Override
	public void run() {
		synchronized(System.out){
			System.out.println("Starting task: " + taskNumber + " sleeping for " + sleepTime);
		}
		try {
			Thread.sleep(sleepTime);
		}catch(InterruptedException ie) {
			ie.printStackTrace();
		}
		synchronized(System.out) {
			System.out.println("Ending Task: " + taskNumber);
		}
	}

	@Override
	public int getTaskType() {
		return -1;
	}

}
