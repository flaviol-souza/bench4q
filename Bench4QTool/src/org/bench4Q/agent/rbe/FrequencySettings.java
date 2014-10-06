package org.bench4Q.agent.rbe;

import java.io.Serializable;

import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.agent.rbe.communication.TypeFrequency;
import org.bench4Q.common.util.Logger;

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

	public static void settings(int index, EB eb, TestPhase testPhase, TypeFrequency type) {

		if (eb.getPropertiesEB() == null) {
			eb.setPropertiesEB(new PropertiesEB());
		}

		eb.getPropertiesEB().setFrenquency(true);
		eb.getPropertiesEB().setIndexEB(index);

		if (TypeFrequency.RAMP.equals(type)) {
			int time = (testPhase.getStdyTime() / qntEbs) * index;
			Logger.getLogger().debug("EB index:"+index+" time:"+time);
			eb.getPropertiesEB().setTimeStart(time);
			eb.getPropertiesEB().setTimeEnd(testPhase.getStdyTime());
		} else if (TypeFrequency.STILE.equals(type)) {
			if (index >= testPhase.getFrequency().getQuantity()) {
				eb.getPropertiesEB().setTimeStart(testPhase.getFrequency().getStartTime());
				eb.getPropertiesEB().setTimeEnd(testPhase.getFrequency().getDurationTime());
			} else {
				eb.getPropertiesEB().setTimeStart(0);
				eb.getPropertiesEB().setTimeEnd(testPhase.getStdyTime());
			}

		}
	}

	/**
	 * Permite definir os tipos de properties
	 * */
	public static PropertiesEB createProperties(int index, TestPhase testPhase, TypeFrequency type, long timeInt) {

		PropertiesEB propertiesEB = new PropertiesEB();
		propertiesEB.isFrenquency = true;

		if (TypeFrequency.RAMP.equals(type)) {
			float reason = testPhase.getStdyTime() / (float)qntEbs;
			int timeStart = (int) (reason * index);
			
			if(testPhase.getFrequency().isPolarity()){
				propertiesEB.setTimeStart(timeStart);
				propertiesEB.setTimeEnd(testPhase.getStdyTime());
			}else{
				propertiesEB.setTimeStart(testPhase.getFrequency().getStartTime());
				propertiesEB.setTimeEnd(testPhase.getFrequency().getStartTime() + timeStart);
			}
		} else if (TypeFrequency.STILE.equals(type)) { 
//			if(testPhase.getFrequency().isPolarity()){
				//os primeiros 
				if (index >= testPhase.getFrequency().getQuantity()) {
					propertiesEB.setTimeStart(0);
					propertiesEB.setTimeEnd(testPhase.getStdyTime());
					Logger.getLogger().debug("normal: "+index);
				} else {
					propertiesEB.setTimeStart(testPhase.getFrequency().getStartTime());
					propertiesEB.setTimeEnd(testPhase.getFrequency().getDurationTime());
					Logger.getLogger().debug("special: "+index);
				}	
//			}else{
//				if (index >= testPhase.getFrequency().getQuantity()) {
//					propertiesEB.setTimeStart(0);
//					propertiesEB.setTimeEnd(testPhase.getStdyTime());
//				} else {
//					propertiesEB.setTimeStart(testPhase.getFrequency()
//							.getStartTime());
//					propertiesEB.setTimeEnd(testPhase.getFrequency()
//							.getDurationTime());
//				}
//			}
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
		
		//System.out.println("EB index:"+index+" timeStart:"+propertiesEB.getTimeStart()+" timeEnd:"+propertiesEB.getTimeEnd());

		return propertiesEB;

	}

}
