package org.bench4Q.agent.rbe.communication;

//Create by ICMC-USP Brazil - Flavio Souza
public class TestFrequency {

	private TypeFrequency type;

	private int startTime;
	private int durationTime;
	private boolean polarity;

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
	public boolean isPolarity() {
		return polarity;
	}

	/**
	 * @param polarity
	 *            the polarity to set
	 */
	public void setPolarity(boolean polarity) {
		this.polarity = polarity;
	}

}
