package com.logmonitor.domain.alert;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class EfficientProxyChainAlert {

	private Long timestamp;

	@SerializedName("message_type")
	private final String messageType = "alert";

	@SerializedName("alert_type")
	private final String alertType = "inefficient_proxy_chain";

	@SerializedName("proxy_chain")
	private LinkedList<String> proxyChain;

	@SerializedName("inefficient_addresses")
	private List<String> inefficientAddresses;

	@SerializedName("efficient_proxy_chains")
	private List<List<String>> efficientProxyChains;

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public LinkedList<String> getProxyChain() {
		return proxyChain;
	}

	public void setProxyChain(LinkedList<String> proxyChain) {
		this.proxyChain = proxyChain;
	}

	public List<String> getInefficientAddresses() {
		return inefficientAddresses;
	}

	public void setInefficientAddresses(List<String> inefficientAddresses) {
		this.inefficientAddresses = inefficientAddresses;
	}

	public List<List<String>> getEfficientProxyChains() {
		return efficientProxyChains;
	}

	public void setEfficientProxyChains(List<List<String>> efficientProxyChains) {
		this.efficientProxyChains = efficientProxyChains;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getAlertType() {
		return alertType;
	}

}
