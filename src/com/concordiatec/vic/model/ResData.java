package com.concordiatec.vic.model;

import com.google.gson.annotations.SerializedName;

public class ResData {
	
	@SerializedName("status")
	private String status;
	@SerializedName("data")
	private Object data;
	@SerializedName("msg")
	private String msg;
	
	public String getStatus() {
		return status;
	}
	public Object getData() {
		return data;
	}
	public String getMsg() {
		return msg;
	}
	
	public String getBodyString() {
		return "ResData [status=" + status + ", data=" + data + ", msg=" + msg + "]";
	}
	
	
}
