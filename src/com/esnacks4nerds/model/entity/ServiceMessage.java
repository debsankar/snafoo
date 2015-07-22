package com.esnacks4nerds.model.entity;

public class ServiceMessage {

	private int code;
	private String message;
	
	public ServiceMessage(int code, String message){
		this.code = code;
		this.message = message;
	}
	public ServiceMessage(){
		
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
