package org.bench4Q.agent.rbe;

public class PropertiesEB {

	protected int indexEB;
	protected long timeStart = 0;
	protected long timeEnd = 0;
	protected boolean isFrenquency = false;

	public int getIndexEB() {
		return indexEB;
	}

	public void setIndexEB(int indexEB) {
		this.indexEB = indexEB;
	}

	public synchronized long getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}

	public synchronized long getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(long timeEnd) {
		this.timeEnd = timeEnd;
	}

	public synchronized boolean isFrenquency() {
		return isFrenquency;
	}

	public void setFrenquency(boolean isFrenquency) {
		this.isFrenquency = isFrenquency;
	}

}
