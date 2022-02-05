package com.pangugle.im.logical.push;

public interface PushSupport {

	public void sendSingleMessage(String token, String notifyTitle, String notifyContent, String payload, long expires) ;
}
