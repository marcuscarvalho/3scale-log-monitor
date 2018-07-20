package com.logmonitor.domain;

import java.util.LinkedList;

public class EfficientProxyChain {

	private int previousSize;
	private LinkedList<String> proxies;

	public EfficientProxyChain(int previousSize, LinkedList<String> proxies) {
		this.previousSize = previousSize;
		this.proxies = proxies;
	}
	
	public int getPreviousSize() {
		return previousSize;
	}

	public LinkedList<String> getProxies() {
		return proxies;
	}

}
