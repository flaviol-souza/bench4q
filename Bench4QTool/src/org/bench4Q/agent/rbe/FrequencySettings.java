package org.bench4Q.agent.rbe;

import java.io.Serializable;

import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.agent.rbe.communication.TypeFrequency;
import org.bench4Q.common.util.Logger;

public class FrequencySettings implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int qntEbs;

	/**
	 * @param qntWorkers
	 *            the qntWorkers to set
	 */
	public static void setQntWorkers(int qntEbs) {
		FrequencySettings.qntEbs = qntEbs;
	}

	/*public static void settings(int index, EB eb, TestPhase testPhase) {
		TypeFrequency type = testPhase.getFrequency().getType();
		//settings(index, eb, testPhase, type);
	}

	/*public static void settings(int index, EB eb, TestPhase testPhase, TypeFrequency type) {

		if (eb.getPropertiesEB() == null) {
			eb.setPropertiesEB(new PropertiesEB());
		}

		eb.getPropertiesEB().setFrenquency(true);
		eb.getPropertiesEB().setIndexEB(index);

		if (TypeFrequency.RAMP.equals(type)) {
			int time = (testPhase.getStdyTime() / qntEbs) * index;
			Logger.getLogger().debug("EB index:" + index + " time:" + time);
			eb.getPropertiesEB().setStartTime(time);
			eb.getPropertiesEB().setEndTime(testPhase.getStdyTime());
			eb.getPropertiesEB().setEndExperimentTime(testPhase.getStdyTime());
		} else if (TypeFrequency.STEP.equals(type)) {
			if (index >= testPhase.getFrequency().getQuantity()) {
				eb.getPropertiesEB().setStartTime(testPhase.getFrequency().getStartTime());
				eb.getPropertiesEB().setEndTime(testPhase.getFrequency().getDurationTime());
				eb.getPropertiesEB().setEndExperimentTime(testPhase.getFrequency().getDurationTime());
			} else {
				eb.getPropertiesEB().setStartTime(0);
				eb.getPropertiesEB().setEndTime(testPhase.getStdyTime());
				eb.getPropertiesEB().setEndExperimentTime(testPhase.getFrequency().getDurationTime());
			}
		} else if(TypeFrequency.PULSE.equals(type)){
			if (index >= testPhase.getFrequency().getQuantity()) {
				eb.getPropertiesEB().setStartTime(testPhase.getFrequency().getStartTime());
				eb.getPropertiesEB().setPauseTime(testPhase.getFrequency().getPauseTime());
				eb.getPropertiesEB().setEndTime(testPhase.getFrequency().getEndTime());
			} else {
				eb.getPropertiesEB().setStartTime(0);
				eb.getPropertiesEB().setEndTime(testPhase.getStdyTime());
				eb.getPropertiesEB().setEndExperimentTime(testPhase.getStdyTime());
			}
		}
	}*/

	/**
	 * Permite definir os tipos de properties
	 * */
	public static PropertiesEB createProperties(int index, TestPhase testPhase, TypeFrequency type, long beginTime) {

		PropertiesEB propertiesEB = new PropertiesEB();
		Logger.getLogger().debug(type.getName());
		propertiesEB.isFrenquency = true;
		propertiesEB.setIndexEB(index);
		
		/*Logger.getLogger().debug("(index): " + index);
		
		Logger.getLogger().debug(type.getName()+" EB index: "+index+
				 " start: "+testPhase.getFrequency().getStartTime()+
				 " end: "+testPhase.getFrequency().getEndTime()+
				 " pause: "+testPhase.getFrequency().getPauseTime()+
				 " experiment: "+testPhase.getExperimentTime());*/
		
		long timeStart = testPhase.getFrequency().getStartTime() * 1000 + beginTime;
		long timeEnd   = testPhase.getFrequency().getEndTime() * 1000 + timeStart;
		long timePause = testPhase.getFrequency().getPauseTime() * 1000;
		long timeExperiment = testPhase.getExperimentTime() * 1000 + beginTime;
		
		

		/*if (TypeFrequency.RAMP.equals(type)) {
			float reason = testPhase.getExperimentTime() / (float) qntEbs;
			timeStart = (int) (reason * index);

			if (testPhase.getFrequency().getPolarity()) {
				propertiesEB.setStartTime(timeStart);
				propertiesEB.setEndTime(timeExperiment);
			} else {
				propertiesEB.setStartTime(timeStart);
				propertiesEB.setEndTime(timeEnd);
			}
		} else */if (TypeFrequency.STEP.equals(type)) {
			if (index >= testPhase.getFrequency().getQuantity()) {
				propertiesEB.setStartTime(beginTime);
				propertiesEB.setEndTime(timeExperiment);
				propertiesEB.setEndExperimentTime(timeExperiment);
				Logger.getLogger().debug("Normal: " + index);
			} else {
				propertiesEB.setStartTime(timeStart);
				propertiesEB.setEndTime(timeEnd);
				propertiesEB.setEndExperimentTime(timeExperiment);
				Logger.getLogger().debug("To Step: " + index);
			}
		} else if (TypeFrequency.PULSE.equals(type)) {
			if (index >= testPhase.getFrequency().getQuantity()) {
				propertiesEB.setStartTime(beginTime);
				propertiesEB.setEndTime(timeExperiment);
				propertiesEB.setEndExperimentTime(timeExperiment);
				Logger.getLogger().debug("Normal: " + index);
			} else {
				propertiesEB.setStartTime(timeStart);
				propertiesEB.setPauseTime(timePause);
				propertiesEB.setEndTime(timeEnd);
				propertiesEB.setEndExperimentTime(timeExperiment);
				Logger.getLogger().debug("To Pulse: " + index);
			}
		}

		 /*Logger.getLogger().debug(type.getName()+" EB index: "+index+
				 " timeStart: "+propertiesEB.getStartTime()+
				 " timeEnd: "+propertiesEB.getEndTime()+
				 " timePause: "+propertiesEB.getPauseTime()+
				 " timeExperiment: "+propertiesEB.getEndExperimentTime());*/

		return propertiesEB;

	}

}
