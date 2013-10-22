package org.bench4Q.agent.rbe.communication;

import java.util.ArrayList;

public class ResultSet implements Sendable {

	private ArrayList<Double> result;

	public ResultSet() {
		super();
		result = new ArrayList<Double>(60);
	}

	public ResultSet(int initLenth) {
		super();
		result = new ArrayList<Double>(initLenth);
	}

	public void addResult(Double value) {
		result.add(value);
	}

	public ArrayList<Double> getResult() {
		return result;
	}

	public void setResult(ArrayList<Double> result) {
		this.result = result;
	}

}
