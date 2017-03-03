package cs455.scaling.util;

public class State {

	private boolean readingState, writingState;
	private final Object lock = new Object();
	private String data;
	public State() {
		readingState = false;
		writingState = false;
		data = null;
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

	public String getData() {
		String returnData;
		synchronized(lock) {
			returnData = data;
			data = null;
		}
		return returnData;
	}

	public void setData(String data) {
		synchronized(lock) {
			this.data = data;
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
