package com.pangugle.framework.socketio;

public interface MessageEncrypt {
	
	public String encrypt(String body);
	public String decrypt(String body);

}
