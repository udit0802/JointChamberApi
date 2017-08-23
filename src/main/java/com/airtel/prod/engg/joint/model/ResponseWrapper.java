package com.airtel.prod.engg.joint.model;

public class ResponseWrapper<T> {

//	private String status;
//	private int responseCode;
	private Status status;
	private Object data;
//	private String errorMessage;
	
	public Status getStatus() {
		return status;
	}
	public ResponseWrapper(Status status, Object data) {
	super();
	this.status = status;
	this.data = data;
}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResponseWrapper [status=" + status + ", data=" + data + "]";
	}
	
}
