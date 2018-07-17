package com.logmonitor.domain;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Stats {

	private Date timestamp;
	@SerializedName("message_type")
	private final String messageType = "stats";
	private int get;
	private int post;
	private int hits;
	@SerializedName("forwarded_hits")
	private int forwardedHits;
	@SerializedName("most_used_proxy")
	private String mostUsedProxy;
	@SerializedName("most_used_proxy_hits")
	private int mostUsedProxyHits;
	private double p95;
	@SerializedName("bad_lines")
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

	/**
	 * The number of GET method request hits
	 * @return
	 */
	public int getGet() {
		return get;
	}

	public void setGet(int get) {
		this.get = get;
	}

	/**
	 * The number of POST method request hits
	 * @return
	 */
	public int getPost() {
		return post;
	}

	public void setPost(int post) {
		this.post = post;
	}

	/**
	 * The total number of HTTP request hits
	 * @return
	 */
	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	/**
	 * The number of requests that have been proxied
	 * @return
	 */
	public int getForwardedHits() {
		return forwardedHits;
	}

	public void setForwardedHits(int forwardedHits) {
		this.forwardedHits = forwardedHits;
	}

	/**
	 * The proxy that forwarded most requests
	 * @return
	 */
	public String getMostUsedProxy() {
		return mostUsedProxy;
	}

	public void setMostUsedProxy(String mostUsedProxy) {
		this.mostUsedProxy = mostUsedProxy;
	}

	/**
	 * The number of forwarded requests
	 * @return
	 */
	public int getMostUsedProxyHits() {
		return mostUsedProxyHits;
	}

	public void setMostUsedProxyHits(int mostUsedProxyHits) {
		this.mostUsedProxyHits = mostUsedProxyHits;
	}

	/**
	 * The 95 percentile request time
	 * @return
	 */
	public double getP95() {
		return p95;
	}

	public void setP95(double p95) {
		this.p95 = p95;
	}

	/**
	 * The number of non-conformant log lines
	 * @return
	 */
	public int getBadLines() {
		return badLines;
	}

	public void setBadLines(int badLines) {
		this.badLines = badLines;
	}

	@Override
	public String toString() {
		return "Stats [timestamp=" + timestamp + ", messageType=" + messageType + ", get=" + get + ", post=" + post
				+ ", hits=" + hits + ", forwardedHits=" + forwardedHits + ", mostUsedProxy=" + mostUsedProxy
				+ ", mostUsedProxyHits=" + mostUsedProxyHits + ", p95=" + p95 + ", badLines=" + badLines + "]";
	}

}
