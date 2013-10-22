package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;

public class TransHome extends Transition {

	public String request(EB eb, String html) {
		return (eb.addIDs(EB.homeURL));
	}

	public boolean toHome() {
		return true;
	}
}
