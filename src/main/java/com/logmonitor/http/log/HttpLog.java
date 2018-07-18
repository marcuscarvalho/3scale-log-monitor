package com.logmonitor.http.log;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * 
 * @author Marcus Carvalho
 *
 */
public class HttpLog {

	private String originAddress;
	private LinkedList<String> proxies;
	private LocalDateTime requestDateTime;
	private String httpMethod;
	private String url;
	private String protocol;
	private int responseStatusCode;
	private double responseTimeInSeconds;

	public String getOriginAddress() {
		return originAddress;
	}

	public void setOriginAddress(String originAddress) {
		this.originAddress = originAddress;
	}

	public LinkedList<String> getProxies() {
		return proxies;
	}

	public void setProxies(LinkedList<String> proxies) {
		this.proxies = proxies;
	}

	public LocalDateTime getRequestDateTime() {
		return requestDateTime;
	}

	public void setRequestDateTime(LocalDateTime requestDateTime) {
		this.requestDateTime = requestDateTime;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getResponseStatusCode() {
		return responseStatusCode;
	}

	public void setResponseStatusCode(int responseStatusCode) {
		this.responseStatusCode = responseStatusCode;
	}

	public double getResponseTimeInSeconds() {
		return responseTimeInSeconds;
	}

	public void setResponseTimeInSeconds(double responseTimeInSeconds) {
		this.responseTimeInSeconds = responseTimeInSeconds;
	}

}
