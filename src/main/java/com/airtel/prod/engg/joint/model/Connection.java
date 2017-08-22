package com.airtel.prod.engg.joint.model;

public class Connection {

	private End end1;
	
	private End end2;

	public End getEnd1() {
		return end1;
	}

	public void setEnd1(End end1) {
		this.end1 = end1;
	}

	public End getEnd2() {
		return end2;
	}

	public void setEnd2(End end2) {
		this.end2 = end2;
	}

	@Override
	public String toString() {
		return "Connection [end1=" + end1 + ", end2=" + end2 + "]";
	}
	
}
