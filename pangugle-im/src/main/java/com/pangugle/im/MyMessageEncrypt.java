package com.pangugle.im;

import com.pangugle.framework.socketio.MessageEncrypt;
import com.pangugle.framework.utils.AESUtils;

public class MyMessageEncrypt implements MessageEncrypt{

	private static String DEFAULT_AES_KEY = "*^jsdfa#1!jsd3j7";
	
	@Override
	public String encrypt(String body) {
		return AESUtils.encrypt(body, DEFAULT_AES_KEY);
	}

	@Override
	public String decrypt(String body) {
		return AESUtils.decrypt(body, DEFAULT_AES_KEY);
	}
	

}
