package org.bench4Q.agent.rbe.util;

public class CharStrPattern extends AbCharStrPattern {
	protected char p; // The character to find.

	public CharStrPattern(char p) {
		this.p = p;
	}

	// Does this character match.
	protected boolean charMatch(char c) {
		return (c == p);
	};

}
