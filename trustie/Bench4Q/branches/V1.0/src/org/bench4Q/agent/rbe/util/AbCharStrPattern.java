package org.bench4Q.agent.rbe.util;

public abstract class AbCharStrPattern extends StringPattern {

	// Search from index start to end, inclusive.
	public int find(String s, int start, int end) {
		int i;

		for (i = start; i <= end; i++) {
			if (charMatch(s.charAt(i))) {
				this.start = i;
				this.end = i;
				return (i);
			}
		}

		return (-1);
	}

	// See if pattern matches exactly characters pos to end, inclusive.
	public boolean matchWithin(String s, int pos, int end) {
		if (charMatch(s.charAt(pos))) {
			this.start = this.end = pos;
			return (true);
		} else {
			return (false);
		}
	}

	// Minimum and maximum lengths.
	protected int minLength() {
		return (1);
	}

	protected int maxLength() {
		return (1);
	}

	// Does this character match.
	protected abstract boolean charMatch(char c);
}
