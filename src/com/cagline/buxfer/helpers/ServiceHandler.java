package com.cagline.buxfer.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ServiceHandler {

	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;

	public ServiceHandler() {

	}

	/*
	 * Making service call
	 * 
	 * @url - url to make request
	 * 
	 * @method - http request method
	 */
	public String makeServiceCall(String url, int method) {
		return this.makeServiceCall(url, method, null, null);
	}

	/*
	 * Making service call
	 * 
	 * @url - url to make request
	 * 
	 * @method - http request method
	 * 
	 * @params - http request params
	 */
	public String makeServiceCall(String url, int method,
			List<NameValuePair> params, Map<String, String> headerValues) {
		try {
			// http client
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			// Checking Http request method type
			if (method == POST) {
				HttpPost httpPost = new HttpPost(url);

				// add header values
				if (headerValues != null) {
					Set<String> keySet = headerValues.keySet();
					for (String key : keySet) {
						String val = headerValues.get(key);
						httpPost.setHeader(key, val);
					}
				}

				// adding post primes
				if (params != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}

				httpResponse = httpClient.execute(httpPost);

			} else if (method == GET) {
				// appending params to url
				if (params != null) {
					String paramString = URLEncodedUtils
							.format(params, "utf-8");
					url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);

				// add header values
				if (headerValues != null) {
					Set<String> keySet = headerValues.keySet();
					for (String key : keySet) {
						String val = headerValues.get(key);
						httpGet.setHeader(key, val);
					}
				}

				httpResponse = httpClient.execute(httpGet);

			}
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;

	}
}
