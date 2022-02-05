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
package com.pangugle.framework.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.utils.FastJsonHelper;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class HttpSesstionManager {
	
	private static Log LOG = LogFactory.getLog(HttpSesstionManager.class);

    private OkHttpClient mHttpClient;

    public static final MediaType JSON_MEDIATYPE = MediaType.parse(HttpMediaType.JSON.getValue());
    public static final MediaType FORM_MEDIATYPE = MediaType.parse(HttpMediaType.FORM.getValue());
    public static final MediaType TEXT_MEDIATYPE = MediaType.parse(HttpMediaType.TEXT.getValue());

    private static final long TIMEOUT = 10;
    private static final int MAX_REQUESTS = 100; // default
    private static final int MAX_REQUESTS_PERHOST = 5; // default

    private long mTimeout = TIMEOUT;
    
   private static final ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_0, TlsVersion.TLS_1_1, TlsVersion.TLS_1_2, TlsVersion.TLS_1_3)
            .cipherSuites(
            		 // TLS 1.2
            		  "TLS_RSA_WITH_AES_256_GCM_SHA384",
            		"TLS_RSA_WITH_AES_128_GCM_SHA256",
            		 "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
            		 "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
            		 "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
            		 "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
            		  "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
            		 "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
            		  "TLS_ECHDE_RSA_WITH_AES_128_GCM_SHA256",
            		 // maximum interoperability
            		 "TLS_RSA_WITH_3DES_EDE_CBC_SHA",
            		  "TLS_RSA_WITH_AES_128_CBC_SHA",
            		  // additionally
            		 "TLS_RSA_WITH_AES_256_CBC_SHA",
            		  "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
            		  "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
            		  "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
            		 "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA"
                    )
            .build();
    
   // 同时支持http 和 https
    private static final List<ConnectionSpec> mConnSpecList = Arrays.asList(spec, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT);
    private static final TrustAllManager trustAllManager = new TrustAllManager();
    private interface MyInternal {
    	public static HttpSesstionManager mgr = new HttpSesstionManager();
    }
    
    public static HttpSesstionManager getInstance() {
        return MyInternal.mgr;
    }

    public HttpSesstionManager() {
        this(TIMEOUT, MAX_REQUESTS, MAX_REQUESTS_PERHOST);
    }

    public HttpSesstionManager(long timeout, int maxRequests, int maxRequestsPerHost) {
        this.mTimeout = timeout;

        try {
			//
			this.mHttpClient = new OkHttpClient.Builder()
			        .readTimeout(mTimeout, TimeUnit.SECONDS)//设置读取超时时间
			        .writeTimeout(mTimeout, TimeUnit.SECONDS)//设置写的超时时间
			        .connectTimeout(mTimeout, TimeUnit.SECONDS)//设置连接超时时间
			        .sslSocketFactory(SSLSocketUtils.createTrustAllSSLFactory(trustAllManager), trustAllManager)
			        .hostnameVerifier(SSLSocketUtils.createTrustAllHostnameVerifier())
			        .connectionSpecs(mConnSpecList)
//			        .certificatePinner(hostnameSSLBuider.build())
			        .build();

			mHttpClient.dispatcher().setMaxRequests(maxRequests);
			mHttpClient.dispatcher().setMaxRequestsPerHost(maxRequestsPerHost);
		} catch (Exception e) {
			LOG.error("init error:", e);
		} 
    }

    public int runningCount() {
        return mHttpClient.dispatcher().runningCallsCount();
    }

    public int queueCount() {
        return mHttpClient.dispatcher().queuedCallsCount();
    }

    public void asyncGet(String url, HttpCallback callback) {
        okhttp3.Request.Builder builder = new Request.Builder().url(url).get();
        Request request = builder.build();
        mHttpClient.newCall(request).enqueue(new Callback() {

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (callback != null) callback.onSuccess(request, response, response.body().bytes());
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            }
            public void onFailure(Call call, IOException e) {
                if (callback != null) callback.onFailure(e);
            }
        });
    }

    public void asyncPost(String url, Map<String, Object> parameter, HttpMediaType type, HttpCallback callback) {
        RequestBody body = createRequestBody(parameter, type);
        okhttp3.Request.Builder builder = new Request.Builder().url(url).post(body);
        Request request = builder.build();
        mHttpClient.newCall(request).enqueue(new Callback() {

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(callback != null) callback.onSuccess(request, response, response.body().bytes());
                } catch (Exception e) {
                	if(callback != null) callback.onFailure(e);
                }
            }

            public void onFailure(Call call, IOException exception) {
            	if(callback != null) callback.onFailure(exception);
            }
        });
    }

    public void asyncPost(String url, String json, HttpCallback callback) {
        RequestBody body = RequestBody.create(JSON_MEDIATYPE, json);

        okhttp3.Request.Builder builder = new Request.Builder().url(url).post(body);
        Request request = builder.build();
        mHttpClient.newCall(request).enqueue(new Callback() {

            public void onResponse(Call call, Response response) throws IOException {
                try {
                	if(callback != null) callback.onSuccess(request, response, response.body().bytes());
                } catch (Exception e) {
                	if(callback != null) callback.onFailure(e);
                }
            }

            public void onFailure(Call call, IOException exception) {
            	if(callback != null) callback.onFailure(exception);
            }
        });
    }

    public byte[] syncPost(String url, HttpMediaType type,Map<String, Object> parameter) throws IOException {
        RequestBody body = createRequestBody(parameter,type);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = mHttpClient.newCall(request).execute();
        return response.body().bytes();
    }

    public byte[] syncPosts(String url, Map<String, Object> parameter) throws IOException {
        RequestBody body = createRequestBody(parameter, HttpMediaType.FORM);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = mHttpClient.newCall(request).execute();
        return response.body().bytes();
    }

    public byte[] syncPost(String url, String jsonStr) throws IOException {
        RequestBody body = RequestBody.create(JSON_MEDIATYPE, jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mHttpClient.newCall(request).execute();
        return response.body().bytes();
    }

    public byte[] syncGet(String url) throws IOException {
        okhttp3.Request.Builder builder = new Request.Builder().url(url).get();
        Request request = builder.build();
        Response response = mHttpClient.newCall(request).execute();
        return response.body().bytes();
    }

    public void syncGet(String url, HttpCallback callback) {
        okhttp3.Request.Builder builder = new Request.Builder().url(url).get();
        try {
            Request request = builder.build();
            Response response = mHttpClient.newCall(request).execute();
            callback.onSuccess(request, response, response.body().bytes());
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

    public boolean head(String url) {
        boolean rs = false;
        okhttp3.Request.Builder builder = new Request.Builder().url(url).get();
        try {
            Request request = builder.build();
            Response response = mHttpClient.newCall(request).execute();
            rs = response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    private RequestBody createRequestBody(Map<String, Object> paramater, HttpMediaType type) {
        if (type == HttpMediaType.JSON) {
            return RequestBody.create(JSON_MEDIATYPE, FastJsonHelper.jsonEncode(paramater));
        } else {
            StringBuffer buffer = new StringBuffer();
            boolean first = true;
            for (String key : paramater.keySet()) {
                if (first) first = false;
                else buffer.append("&");
                Object value = paramater.get(key);
                String strValue;
                if (value instanceof String) strValue = (String) value;
                else strValue = FastJsonHelper.jsonEncode(value);
                String encodeValue;
                try {
                    encodeValue = URLEncoder.encode(strValue, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    encodeValue = strValue;
                }
                buffer.append(key).append("=").append(encodeValue);
            }
            if (type == HttpMediaType.FORM) {
                return RequestBody.create(FORM_MEDIATYPE, buffer.toString());
            } else {
                return RequestBody.create(TEXT_MEDIATYPE, buffer.toString());
            }
        }
    }

    private void test(String url, String key) {
        System.out.println("start request name = " + key);
        asyncGet(url, new HttpCallback() {

            @Override
            public void onFailure(Throwable e) {
                System.out.println("failure request name = " + key);
            }

            @Override
            public void onSuccess(Request request, Response response, byte[] data) {
                System.out.println("success request name = " + key + ", data = " + new String(data));

            }
        });
        System.out.println("end request name = " + key);
    }

}
