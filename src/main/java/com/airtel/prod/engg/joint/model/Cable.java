package com.airtel.prod.engg.joint.model;

public class Cable {

	private String cableId;
	
	private String cableDirection;
	
	private int cableType;
	
	private int cableOrder;

	public String getCableId() {
		return cableId;
	}

	public void setCableId(String cableId) {
		this.cableId = cableId;
	}

	public String getCableDirection() {
		return cableDirection;
	}

	public void setCableDirection(String cableDirection) {
		this.cableDirection = cableDirection;
	}

	public int getCableType() {
		return cableType;
	}

	public void setCableType(int cableType) {
		this.cableType = cableType;
	}

	public int getCableOrder() {
		return cableOrder;
	}

	public void setCableOrder(int cableOrder) {
		this.cableOrder = cableOrder;
	}

	@Override
	public String toString() {
		return "Cable [cableId=" + cableId + ", cableDirection=" + cableDirection + ", cableType=" + cableType
				+ ", cableOrder=" + cableOrder + "]";
	}
	
}
