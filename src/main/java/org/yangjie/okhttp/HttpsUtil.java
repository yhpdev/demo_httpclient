package org.yangjie.okhttp;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * HTTPS请求工具类
 * 可添加证书功能通讯
 * @author YangJie [2016年3月18日 下午3:45:26]
 */
public class HttpsUtil {

	private static OkHttpClient client = new OkHttpClient();
	
	private static final MediaType jsonMediaType =  MediaType.parse("application/json; charset=utf-8");
	private static final MediaType xmlMediaType =  MediaType.parse("application/xml; charset=utf-8");
	
	
	/**
     * 发送post请求, 内容格式为json
     * @param url
     * @param body
     * @return
     */
    public static String postJson(SSLSocketFactory sslSocketFactory, String url, String body) {
    	try {
	    	client = client.newBuilder().sslSocketFactory(sslSocketFactory).build();
			RequestBody requestBody = RequestBody.create(jsonMediaType, body);
			Request request = new Request.Builder().url(url).post(requestBody).build();
			return client.newCall(request).execute().body().string();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 发送post请求, 内容格式为xml
     * @param url
     * @param body
     * @return
     */
    public static String postXml(SSLSocketFactory sslSocketFactory, String url, String body) {
    	try {
    		client = client.newBuilder().sslSocketFactory(sslSocketFactory).build();
    		RequestBody requestBody = RequestBody.create(xmlMediaType, body);
    		Request request = new Request.Builder().url(url).post(requestBody).build();
    		return client.newCall(request).execute().body().string();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
	
    
	/**
	 * 获取SSLSocketFactory
	 * @author YangJie [2016年5月18日 下午3:52:03]
	 * @param filePath 证书路径
	 * @param password 证书密码
	 * @throws Exception 
	 */
    public static SSLSocketFactory getSSLSocketFactory(String filePath, String password) {
    	try { // 加载证书
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(new File(filePath));
	        keyStore.load(instream, password.toCharArray());
	        // 证书加入密钥库
	        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	        keyManagerFactory.init(keyStore, password.toCharArray());
	        // 创建ssl上下文
	        SSLContext sslContext = SSLContext.getInstance("TLSv1");
	        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );  
	        secureRandom.setSeed("moko".getBytes());  
	        sslContext.init(keyManagerFactory.getKeyManagers(), null, secureRandom);
	        return sslContext.getSocketFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
	}


}
