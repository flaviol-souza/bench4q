package org.bench4Q.agent.rbe;

import java.io.Serializable;

import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.agent.rbe.communication.TypeFrequency;

public class FrequencySettings implements Serializable{

	private static int qntWorkers;
	
	/**
	 * @param qntWorkers the qntWorkers to set
	 */
	public static void setQntWorkers(int qntWorkers) {
		FrequencySettings.qntWorkers = qntWorkers;
	}


	public static void settings(int index, Workers workers, TestPhase testPhase){
		TypeFrequency type = testPhase.getFrequency().getType();
		
		if(TypeFrequency.RAMP.equals(type)){
			workers.m_startTime = (workers.m_stdyTime/qntWorkers)*index;
		}else if(TypeFrequency.STILE.equals(type)){
			if(index >= testPhase.getFrequency().getQuantity()){
				workers.m_startTime = testPhase.getFrequency().getStartTime();
			}else{
				workers.m_startTime = 0;
			}
		}
	}
	
}
