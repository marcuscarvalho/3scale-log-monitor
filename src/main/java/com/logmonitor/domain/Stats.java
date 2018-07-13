package com.logmonitor.domain;

import java.util.Date;

public class Stats {

	private Date timestamp;
	private final String messageType = "stats";
	private int get;
	private int post;
	private int hits;
	private int forwardedHits;
	private String mostUsedProxy;
	private int mostUsedProxyHits;
	private float p95;
	private int badLines;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessageType() {
		return messageType;
	}

	public int getGet() {
		return get;
	}

	public void setGet(int get) {
		this.get = get;
	}

	public int getPost() {
		return post;
	}

	public void setPost(int post) {
		this.post = post;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getForwardedHits() {
		return forwardedHits;
	}

	public void setForwardedHits(int forwardedHits) {
		this.forwardedHits = forwardedHits;
	}

	public String getMostUsedProxy() {
		return mostUsedProxy;
	}

	public void setMostUsedProxy(String mostUsedProxy) {
		this.mostUsedProxy = mostUsedProxy;
	}

	public int getMostUsedProxyHits() {
		return mostUsedProxyHits;
	}

	public void setMostUsedProxyHits(int mostUsedProxyHits) {
		this.mostUsedProxyHits = mostUsedProxyHits;
	}

	public float getP95() {
		return p95;
	}

	public void setP95(float p95) {
		this.p95 = p95;
	}

	public int getBadLines() {
		return badLines;
	}

	public void setBadLines(int badLines) {
		this.badLines = badLines;
	}

}
