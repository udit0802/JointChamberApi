package com.airtel.prod.engg.joint.model;

import java.util.List;

public class Joint {
	
	private String jointId;

	private int noOfCables;
	
	private long jointOrder;
	
	private List<Cable> cableInfo;
	
	private List<Connection> connections;

	public String getJointId() {
		return jointId;
	}

	public void setJointId(String jointId) {
		this.jointId = jointId;
	}

	public int getNoOfCables() {
		return noOfCables;
	}

	public void setNoOfCables(int noOfCables) {
		this.noOfCables = noOfCables;
	}

	public long getJointOrder() {
		return jointOrder;
	}

	public void setJointOrder(long jointOrder) {
		this.jointOrder = jointOrder;
	}

	public List<Cable> getCableInfo() {
		return cableInfo;
	}

	public void setCableInfo(List<Cable> cableInfo) {
		this.cableInfo = cableInfo;
	}

	public List<Connection> getConnections() {
		return connections;
	}

	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}

	@Override
	public String toString() {
		return "Joint [jointId=" + jointId + ", noOfCables=" + noOfCables + ", jointOrder=" + jointOrder
				+ ", cableInfo=" + cableInfo + ", connections=" + connections + "]";
	}
	
}
