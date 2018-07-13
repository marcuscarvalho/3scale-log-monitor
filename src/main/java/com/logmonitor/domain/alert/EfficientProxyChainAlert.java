package com.logmonitor.domain.alert;

import java.util.Date;

public class EfficientProxyChainAlert {

	private Date timestamp;
	private final String messageType = "alert";
	private final String alertType = "ok";
	private String[] proxyChain;
	private String[] inefficientAddresses;
	private String[] efficient_proxy_chains;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String[] getProxyChain() {
		return proxyChain;
	}

	public void setProxyChain(String[] proxyChain) {
		this.proxyChain = proxyChain;
	}

	public String[] getInefficientAddresses() {
		return inefficientAddresses;
	}

	public void setInefficientAddresses(String[] inefficientAddresses) {
		this.inefficientAddresses = inefficientAddresses;
	}

	public String[] getEfficient_proxy_chains() {
		return efficient_proxy_chains;
	}

	public void setEfficient_proxy_chains(String[] efficient_proxy_chains) {
		this.efficient_proxy_chains = efficient_proxy_chains;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getAlertType() {
		return alertType;
	}

}
