package org.bench4Q.agent.rbe.communication;

import java.util.ArrayList;

//Create by ICMC-USP Brazil - Flavio Souza
public enum TypeFrequency {

	STILE("Stile", new String[] { "Start Time", "Duration Stile", "Polarity",
			"Quantity" }), RAMP("Ramp", new String[] {});

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

	public static String[] getAttributes(String name) {
		for (TypeFrequency type : TypeFrequency.values()) {
			if (type.getName().equals(name)) {
				return type.getAttributes();
			}
		}
		return null;
	}

	public Object getValueAt(ArrayList data, int rowIndex, int columnIndex) {

		if (TypeFrequency.RAMP.equals(this)) {
			return this.getValueAtRamp(data, rowIndex, columnIndex);
		} else if (TypeFrequency.STILE.equals(this)) {
			return this.getValueAtStile(data, rowIndex, columnIndex);
		}

		return null;
	}

	public void setValueAt(ArrayList data, Object value, int row, int col) {

		if (TypeFrequency.RAMP.equals(this)) {
			this.setValueAtRamp(data, value, row, col);
		} else if (TypeFrequency.STILE.equals(this)) {
			this.setValueAtStile(data, value, row, col);
		}

		// resetShowPanel();
	}

	private void setValueAtRamp(ArrayList data, Object value, int row, int col) {

		if (col == 0) {
			((TestPhase) data.get(row)).getFrequency().setStartTime(
					Integer.valueOf((String) value));
		} else if (col == 1) {
			((TestPhase) data.get(row)).getFrequency().setDurationTime(
					Integer.valueOf((String) value));
		} else if (col == 2) {
			if (((String) value).equals("-"))
				((TestPhase) data.get(row)).getFrequency().setPolarity(false);
			else
				((TestPhase) data.get(row)).getFrequency().setPolarity(true);
		} else {

		}
	}

	private void setValueAtStile(ArrayList data, Object value, int row, int col) {

		if (col == 0) {
			((TestPhase) data.get(row)).getFrequency().setStartTime(
					Integer.valueOf((String) value));
		} else if (col == 1) {
			((TestPhase) data.get(row)).getFrequency().setDurationTime(
					Integer.valueOf((String) value));
		} else if (col == 2) {
			if (((String) value).equals("-"))
				((TestPhase) data.get(row)).getFrequency().setPolarity(false);
			else
				((TestPhase) data.get(row)).getFrequency().setPolarity(true);
		} else if (col == 3) {
			((TestPhase) data.get(row)).getFrequency().setQuantity(
					Integer.valueOf((String) value));
		} else {
			
		}
	}

	private Object getValueAtRamp(ArrayList data, int rowIndex, int columnIndex) {

		if (columnIndex == 0) {
			return ((TestPhase) data.get(rowIndex)).getFrequency()
					.getStartTime();
		} else if (columnIndex == 1) {
			return ((TestPhase) data.get(rowIndex)).getFrequency()
					.getDurationTime();
		} else if (columnIndex == 2) {
			if (((TestPhase) data.get(rowIndex)).getFrequency().isPolarity())
				return "+";
			else
				return "-";
		} else {
			return null;
		}
	}

	private Object getValueAtStile(ArrayList data, int rowIndex, int columnIndex) {

		if (columnIndex == 0) {
			return ((TestPhase) data.get(rowIndex)).getFrequency()
					.getStartTime();
		} else if (columnIndex == 1) {
			return ((TestPhase) data.get(rowIndex)).getFrequency()
					.getDurationTime();
		} else if (columnIndex == 2) {
			if (((TestPhase) data.get(rowIndex)).getFrequency().isPolarity())
				return "+";
			else
				return "-";
		} else if (columnIndex == 3) {
			return ((TestPhase) data.get(rowIndex)).getFrequency().getQuantity();
		} else {
			return null;
		}
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
