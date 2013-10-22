package org.bench4Q.agent.rbe.communication;

import java.util.ArrayList;

public class ErrorSet implements Sendable {

	private ArrayList<EBError> error;

	public ErrorSet() {
		super();
		error = new ArrayList<EBError>(60);
	}

	public ErrorSet(int initLenth) {
		super();
		error = new ArrayList<EBError>(initLenth);
	}

	public void add(EBError value) {
		error.add(value);
	}

	public ArrayList<EBError> getResult() {
		return error;
	}

	public void setResult(ArrayList<EBError> error) {
		this.error = error;
	}

}
