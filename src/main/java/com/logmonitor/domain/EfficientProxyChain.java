package com.logmonitor.domain;

import java.util.LinkedList;

public class EfficientProxyChain {

	private LinkedList<String> proxySize;
	private LinkedList<String> efficientProxies;

	public EfficientProxyChain(LinkedList<String> proxySize, LinkedList<String> efficientProxies) {
		this.proxySize = proxySize;
		this.efficientProxies = efficientProxies;
	}

	public LinkedList<String> getProxySize() {
		return proxySize;
	}

	public void setProxySize(LinkedList<String> proxySize) {
		this.proxySize = proxySize;
	}

	public LinkedList<String> getEfficientProxies() {
		return efficientProxies;
	}

	public void setEfficientProxies(LinkedList<String> efficientProxies) {
		this.efficientProxies = efficientProxies;
	}

}
