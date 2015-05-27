package org.bench4Q.agent.rbe;

public class PropertiesEB {

	protected int indexEB;
	protected long startTime = 0;
	protected long endTime = 0;
	protected long startExperimentTime = 0;
	protected long endExperimentTime = 0;
	protected long pauseTime = -1;
	protected boolean isFrenquency = false;
	
	public long getStartExperimentTime() {
		return startExperimentTime;
	}

	public void setStartExperimentTime(long startExperimentTime) {
		this.startExperimentTime = startExperimentTime;
	}

	public long getPauseTime() {
		return pauseTime;
	}

	public void setPauseTime(long timePeriod) {
		this.pauseTime = timePeriod;
	}

	public int getIndexEB() {
		return indexEB;
	}

	public void setIndexEB(int indexEB) {
		this.indexEB = indexEB;
	}

	public synchronized long getStartTime() {
		return startTime;
	}

	public void setStartTime(long timeStart) {
		this.startTime = timeStart;
	}

	public synchronized long getEndTime() {
		return endTime;
	}

	public void setEndTime(long timeEnd) {
		this.endTime = timeEnd;
	}

	public long getEndExperimentTime() {
		return endExperimentTime;
	}

	public void setEndExperimentTime(long endExperimentTime) {
		this.endExperimentTime = endExperimentTime;
	}

	public synchronized boolean isFrenquency() {
		return isFrenquency;
	}

	public void setFrenquency(boolean isFrenquency) {
		this.isFrenquency = isFrenquency;
	}

}
