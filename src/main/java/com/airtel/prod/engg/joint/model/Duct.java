package com.airtel.prod.engg.joint.model;

import java.util.Map;

public class Duct {
	
	private String id;
	
	private String ductId;

	private String color;
	
	private int noOfCables;
	
	private Map<String,Integer> cableLoopMap;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDuctId() {
		return ductId;
	}

	public void setDuctId(String ductId) {
		this.ductId = ductId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getNoOfCables() {
		return noOfCables;
	}

	public void setNoOfCables(int noOfCables) {
		this.noOfCables = noOfCables;
	}

	public Map<String, Integer> getCableLoopMap() {
		return cableLoopMap;
	}

	public void setCableLoopMap(Map<String, Integer> cableLoopMap) {
		this.cableLoopMap = cableLoopMap;
	}

	@Override
	public String toString() {
		return "Duct [id=" + id + ", ductId=" + ductId + ", color=" + color + ", noOfCables=" + noOfCables
				+ ", cableLoopMap=" + cableLoopMap + "]";
	}
}
