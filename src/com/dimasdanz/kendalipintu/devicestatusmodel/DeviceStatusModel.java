package com.dimasdanz.kendalipintu.devicestatusmodel;

public class DeviceStatusModel {
	private int password_attempts;
	private boolean status;
	private boolean condition;
	
	public DeviceStatusModel(boolean status, int password_attempts, boolean condition) {
		this.password_attempts = password_attempts;
		this.status = status;
		this.condition = condition;
	}
	
	public int getPasswordAttempts(){
		return password_attempts;
	}
	
	public boolean getStatus(){
		return status;
	}
	
	public boolean getCondition(){
		return condition;
	}
}
