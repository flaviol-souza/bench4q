package org.bench4Q.agent.rbe.communication;

import java.util.ArrayList;

import org.bench4Q.common.util.Logger;

//Create by ICMC-USP Brazil - Flavio Souza
public enum TypeFrequency {

	STEP("Step", new String[] { "Start Time", "Duration Step", "Polarity", "Quantity" }), //
	PULSE("Pulse", new String[] { "Start Time", "Period", "Sleep", "Quantity" });//,
	//RAMP("Ramp", new String[] { "Start Time", "Quantity", "Polarity" });
	

	private final String name;
	private final String[] attributes;

	private TypeFrequency(String name, String[] attributes) {
		this.name = name;
		this.attributes = attributes;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the attributes
	 */
	public String[] getAttributes() {
		return attributes;
	}

	public static TypeFrequency getType(String name) {
		for (TypeFrequency type : TypeFrequency.values()) {
			if (type.getName().equals(name)) {
				return type;
			}
		}
		return null;
	}

}
