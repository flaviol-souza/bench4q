package org.bench4Q.agent.rbe.communication;

import java.io.Serializable;

//Create by ICMC-USP Brazil - Flavio Souza
public class TestFrequency implements Serializable {

	private TypeFrequency type;

	private int startTime;
	private int endTime;
	private int pauseTime;
	private int durationTime;
	private boolean polarity;
	private int quantity;

	public TestFrequency() {
		// TODO Auto-generated constructor stub
		pauseTime = -1;
	}
	/**
	 * @return the type
	 */
	public TypeFrequency getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(TypeFrequency type) {
		this.type = type;
	}

	/**
	 * @return the startTime
	 */
	public int getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the durationTime
	 */
	public int getDurationTime() {
		return durationTime;
	}

	/**
	 * @param durationTime
	 *            the durationTime to set
	 */
	public void setDurationTime(int durationTime) {
		this.durationTime = durationTime;
	}

	/**
	 * @return the polarity
	 */
	public boolean getPolarity() {
		return polarity;
	}

	/**
	 * @param polarity
	 *            the polarity to set
	 */
	public void setPolarity(boolean polarity) {
		this.polarity = polarity;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPauseTime() {
		return pauseTime;
	}

	public void setPauseTime(int periodTime) {
		this.pauseTime = periodTime;
	}

	
}
