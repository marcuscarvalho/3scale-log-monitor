package com.logmonitor.http.log;

import java.time.LocalDateTime;

public class LogLine {

	private String client;
	private String[] proxies;
	private LocalDateTime dateTime;
	private String httpMethod;
	private String url;
	private String protocol;
	private int responseStatusCode;
	private float responseTimeInSeconds;

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String[] getProxies() {
		return proxies;
	}

	public void setProxies(String[] proxies) {
		this.proxies = proxies;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
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

	public float getResponseTimeInSeconds() {
		return responseTimeInSeconds;
	}

	public void setResponseTimeInSeconds(float responseTimeInSeconds) {
		this.responseTimeInSeconds = responseTimeInSeconds;
	}

}
