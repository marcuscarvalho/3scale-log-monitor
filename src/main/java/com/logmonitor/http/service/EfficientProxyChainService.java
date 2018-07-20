package com.logmonitor.http.service;

import java.util.LinkedList;

import com.google.gson.Gson;

/**
 * Check and alert when an inefficient proxy is observed.
 * 
 * @author Marcus Carvalho
 *
 */
public interface EfficientProxyChainService {

	void findEfficientProxies(LinkedList<String> proxies);

	void alert(LinkedList<String> proxies, String originAddress, Gson gson);

}
