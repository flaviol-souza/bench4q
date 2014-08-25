package org.bench4Q.agent.rbe;

import java.io.Serializable;

import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.agent.rbe.communication.TypeFrequency;

public class FrequencySettings implements Serializable{

	private static int qntEbs;
	
	/**
	 * @param qntWorkers the qntWorkers to set
	 */
	public static void setQntWorkers(int qntEbs) {
		FrequencySettings.qntEbs = qntEbs;
	}


	public static void settings(int index, EB eb, TestPhase testPhase){
		TypeFrequency type = testPhase.getFrequency().getType();
		eb.isFrenquency = true;

		if(TypeFrequency.RAMP.equals(type)){
			eb.timeStart = (testPhase.getStdyTime()/qntEbs) * index;
			eb.timeEnd = testPhase.getStdyTime();
		}else if(TypeFrequency.STILE.equals(type)){
			if(index >= testPhase.getFrequency().getQuantity()){
				eb.timeStart = testPhase.getFrequency().getStartTime();
				eb.timeEnd = testPhase.getFrequency().getDurationTime();
			}else{
				eb.timeStart = 0;
				eb.timeEnd = testPhase.getStdyTime();
			}
			
		}
	}
	
}
