package cs455.scaling.threads;

import java.util.Random;

public class TestThreadPool {



	public static void main(String[] args) {
		ThreadPoolManager manager = new ThreadPoolManager();
		manager.initializeThreadPool(10);
		for(int i = 0; i < 100; i++) {
			manager.addTask(new DummyTask(i, (new Random()).nextInt(10001)));
		}
	}
}
