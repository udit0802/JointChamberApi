package com.airtel.prod.engg.joint.model;

public class ConnectionDb {

	private String end1;
	
	private String end2;

	public String getEnd1() {
		return end1;
	}

	public void setEnd1(String end1) {
		this.end1 = end1;
	}

	public String getEnd2() {
		return end2;
	}

	public void setEnd2(String end2) {
		this.end2 = end2;
	}

	@Override
	public String toString() {
		return "ConnectionDb [end1=" + end1 + ", end2=" + end2 + "]";
	}
}
