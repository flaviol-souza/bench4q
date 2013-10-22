package org.bench4Q.agent.rbe.communication;

public class TestPhase implements Sendable {
	private int baseLoad;
	private int randomLoad;
	
	private int rate;
	private int stdyTime;
	private int triggerTime;

	public int getBaseLoad() {
		return baseLoad;
	}

	public void setBaseLoad(int base) {
		this.baseLoad = base;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getStdyTime() {
		return stdyTime;
	}

	public void setStdyTime(int time) {
		this.stdyTime = time;
	}

	public int getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(int triggerTime) {
		this.triggerTime = triggerTime;
	}

	public int getRandomLoad() {
		return randomLoad;
	}

	public void setRandomLoad(int randomLoad) {
		this.randomLoad = randomLoad;
	}

}
