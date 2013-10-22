package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;

public abstract class Transition {

	// Computes the HTTP request.
	public abstract String request(EB eb, String html);

	// Post-process received HTML
	public void postProcess(EB eb, String html) {
	};

	// Return true if the transition is to the home page.
	public boolean toHome() {
		return false;
	};

}
