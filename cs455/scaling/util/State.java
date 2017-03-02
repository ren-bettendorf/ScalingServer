package cs455.scaling.util;

public class State {

	private boolean readingState, writingState;
	private final Object lock = new Object();

	public State() {
		readingState = false;
		writingState = false;
	}

	public boolean getReadingState() {
		synchronized(lock) {
			return readingState;
		}
	} 

	public boolean getWritingState() {
		synchronized(lock) {
			return writingState;
		}
	}

	public void setReadingState(boolean state) {
		synchronized(lock) {
			readingState = state;
		}
	}

	public void setWritingState(boolean state) {
		synchronized(lock) {
			writingState = state;
		}
	}
}
