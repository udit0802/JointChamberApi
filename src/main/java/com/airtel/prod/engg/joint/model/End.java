package com.airtel.prod.engg.joint.model;

public class End {

	private String cableId;
	
	private String tubeId;
	
	private String color;

	public String getCableId() {
		return cableId;
	}

	public void setCableId(String cableId) {
		this.cableId = cableId;
	}

	public String getTubeId() {
		return tubeId;
	}

	public void setTubeId(String tubeId) {
		this.tubeId = tubeId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "End [cableId=" + cableId + ", tubeId=" + tubeId + ", color=" + color + "]";
	}
	
}
