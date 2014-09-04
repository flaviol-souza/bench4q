package org.bench4Q.agent.rbe;

import java.io.Serializable;

import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.agent.rbe.communication.TypeFrequency;

public class FrequencySettings implements Serializable {

	private static int qntEbs;

	/**
	 * @param qntWorkers
	 *            the qntWorkers to set
	 */
	public static void setQntWorkers(int qntEbs) {
		FrequencySettings.qntEbs = qntEbs;
	}

	public static void settings(int index, EB eb, TestPhase testPhase) {
		TypeFrequency type = testPhase.getFrequency().getType();
		settings(index, eb, testPhase, type);
	}

	public static void settings(int index, EB eb, TestPhase testPhase,
			TypeFrequency type) {

		if (eb.getPropertiesEB() == null) {
			eb.setPropertiesEB(new PropertiesEB());
		}

		eb.getPropertiesEB().setFrenquency(true);
		eb.getPropertiesEB().setIndexEB(index);

		if (TypeFrequency.RAMP.equals(type)) {
			eb.getPropertiesEB().setTimeStart(
					(testPhase.getStdyTime() / qntEbs) * index);
			eb.getPropertiesEB().setTimeEnd(testPhase.getStdyTime());
		} else if (TypeFrequency.STILE.equals(type)) {
			if (index >= testPhase.getFrequency().getQuantity()) {
				eb.getPropertiesEB().setTimeStart(
						testPhase.getFrequency().getStartTime());
				eb.getPropertiesEB().setTimeEnd(
						testPhase.getFrequency().getDurationTime());
			} else {
				eb.getPropertiesEB().setTimeStart(0);
				eb.getPropertiesEB().setTimeEnd(testPhase.getStdyTime());
			}

		}
	}

	public static PropertiesEB createProperties(int index, TestPhase testPhase,
			TypeFrequency type, long timeInt) {

		PropertiesEB propertiesEB = new PropertiesEB();
		propertiesEB.isFrenquency = true;

		if (TypeFrequency.RAMP.equals(type)) {
			propertiesEB.setTimeStart((testPhase.getStdyTime() / qntEbs)
					* index);
			propertiesEB.setTimeEnd(testPhase.getStdyTime());
		} else if (TypeFrequency.STILE.equals(type)) {
			if (index >= testPhase.getFrequency().getQuantity()) {
				propertiesEB.setTimeStart(0);
				propertiesEB.setTimeEnd(testPhase.getStdyTime());
			} else {
				propertiesEB.setTimeStart(testPhase.getFrequency()
						.getStartTime());
				propertiesEB.setTimeEnd(testPhase.getFrequency()
						.getDurationTime());
			}

		}

		long timeStart = (propertiesEB.getTimeStart() * 1000) + timeInt;
		long timeEnd = (propertiesEB.getTimeEnd() * 1000) + timeStart;

		propertiesEB.setTimeStart(timeStart);
		
		long timeStudy = (testPhase.getStdyTime() * 1000) + timeInt;
		if(timeEnd > timeStudy){
			propertiesEB.setTimeEnd(timeStudy);
		}else{
			propertiesEB.setTimeEnd(timeEnd);
		}

		return propertiesEB;

	}

}
