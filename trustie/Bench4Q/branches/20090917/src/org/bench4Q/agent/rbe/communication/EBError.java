package org.bench4Q.agent.rbe.communication;

class EBError implements Sendable {
	public String message;
	public String url;

	public EBError(String message, String url) {
		this.message = message;
		this.url = url;
	}

	public String toString() {
		return ("EB Error: " + message + "(" + url + ")");
	}
}
