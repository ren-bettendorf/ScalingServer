package cs455.scaling.threads;

import java.util.Random;

public class TestThreadPool {



	public static void main(String[] args) {
		ThreadPoolManager manager = new ThreadPoolManager(10);
		for(int i = 0; i < 100; i++) {
			manager.addTask(new DummyTask(i, (new Random()).nextInt(1001)));
		}
		manager.initializeThreadPool(10);
		manager.startTask();

	}

}
