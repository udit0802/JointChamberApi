package com.airtel.prod.engg.joint.model;

public class ResponseWrapper<T> {

	private String status;
	private int responseCode;
	private Object data;
	private String errorMessage;
	
	public ResponseWrapper() {
		this.status = "success";
		this.responseCode = 0;
	}
	public ResponseWrapper(String status, int responseCode, Object data, String errorMessage) {
		super();
		this.status = status;
		this.responseCode = responseCode;
		this.data = data;
		this.errorMessage = errorMessage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String toString() {
		return "ResponseWrapper [status=" + status + ", responseCode=" + responseCode + ", data=" + data
				+ ", errorMessage=" + errorMessage + "]";
	}
	
}
