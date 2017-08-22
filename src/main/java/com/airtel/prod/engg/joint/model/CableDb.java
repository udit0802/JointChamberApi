package com.airtel.prod.engg.joint.model;

public class CableDb extends Cable {

	private String tubeId;
	
	private String color;
	
	private String ductPk;

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

	public String getDuctPk() {
		return ductPk;
	}

	public void setDuctPk(String ductPk) {
		this.ductPk = ductPk;
	}

	@Override
	public String toString() {
		return "CableDb [tubeId=" + tubeId + ", color=" + color + ", ductPk=" + ductPk + "]";
	}
}
