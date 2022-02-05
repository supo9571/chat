/*
 * Copyright (C) 2019  即时通讯网(www.pangugle.com) & Jack Pangugle.
 * The pangugle project. All rights reserved.
 * 
 * 【本产品为著作权产品，合法授权后请放心使用，禁止外传！】
 * 【本次授权给：<xxx网络科技有限公司>，授权编号：<授权编号-xxx>】
 * 
 * 本系列产品在国家版权局的著作权登记信息如下：
 * 1）国家版权局登记名（简称）和证书号：Project_name（软著登字第xxxxx号）
 * 著作权所有人：厦门盘古网络科技有限公司
 * 
 * 违法或违规使用投诉和举报方式：
 * 联系邮件：2624342267@qq.com
 * 联系微信：pangugle
 * 联系QQ：2624342267
 * 官方社区：http://www.pangugle.com
 */
package com.pangugle.framework.utils;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.pangugle.framework.http.HttpCallback;
import com.pangugle.framework.http.HttpMediaType;
import com.pangugle.framework.http.HttpSesstionManager;

import okhttp3.Request;
import okhttp3.Response;

public class WeixinAESUtils {

	private static final String KEY_ALGORITHM = "AES";
	private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}


	/**
	 * AES解密
	 * 
	 * @param content
	 *            密文
	 * @return
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchProviderException
	 */
	public static byte[] decrypt(byte[] content, byte[] key, byte[] iv) {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			Key sKeySpec = new SecretKeySpec(key, KEY_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(iv));// 初始化
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 生成iv
	private static AlgorithmParameters generateIV(byte[] iv) throws Exception {
		AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
		params.init(new IvParameterSpec(iv));
		return params;
	}
	
	private static void test1()
	{
		String input = "2r7Wv0PN9go8aSzIoRl6PFRS9tgrTpLjCyN/bZpjb+CcoMHRcP6W0qyGd/fHGfzezRumA3yRHRtFTUirq/No8jG1emclGAsiicFX26jwzzki7EBb9l+rPGcxwGNNUOq1PR6YcpChspBhOiptnPb/64hI5l8x7kv1bkIyXoSfJw6kxTdyDlNE75lXvBFxMCbdTewVkRlXAGh/A9vFDSHY/Az2Uo7OENQAi/dHOEMDRyPLo9p3bjpPqhBDhXVuB6ldjturvlZxu2v9TuRZMbEptqLaYz9zMRHUrEmnCbHt2JM6Ku6hCjNcYbbbsskjfT1mORIe9dCG3ixcxGZitpdtmTWKqPClgrrZ5aV1W5RgewiOPMNzNpoy0VUmYU5O4911Q6O8iIlmuOzvYtEThlnJi7iWhCF2lnRT9+LLBuSulnyT2yvjQCok1+Q6BkHrCnFsZ4vh/WgVS3AuPJnl1Sz3iFZbEs7PZNDPE/iIpp2Nx1MfR/DVtF9SrbBNAwvTx22dYqtAzmQ1ANm01xZAZynlqdEQhwpy8XItlzflOzqc5i4=";
		String key = "wqoJCmpX0tHuKqA22wfEyA==";
		String iv = "Qi/sefNzpf2zy0c5Moll0g==";
		byte[] resultByte = WeixinAESUtils.decrypt(Base64.decodeBase64(input), Base64.decodeBase64(key), Base64.decodeBase64(iv));
		System.out.println(new String(resultByte));
		
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("input", input);
		json.put("key", key);
		json.put("iv", iv);
		
		HttpSesstionManager http = HttpSesstionManager.getInstance();
		http.asyncPost("http://192.168.1.171:7101/services2/special/aes/decrypt", json, HttpMediaType.JSON, new HttpCallback() {
			
			public void onSuccess(Request request, Response response, byte[] data) {
				System.out.println("=========>" + new String(data));

            }
		});
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		test1();
	}

}
