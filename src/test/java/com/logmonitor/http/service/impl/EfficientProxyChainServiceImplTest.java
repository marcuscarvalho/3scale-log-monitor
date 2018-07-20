package com.logmonitor.http.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;

public class EfficientProxyChainServiceImplTest {

	@Test
	public void testEfficientProxyChain() {

		String originAddress1 = "100.1.10.11";
		LinkedList<String> proxies1 = new LinkedList<>();
		proxies1.add("89.2.1.13");
		proxies1.add("64.200.1.20");
		proxies1.add("10.1.10.16");

		String originAddress2 = "80.139.20.2";
		LinkedList<String> proxies2 = new LinkedList<>();
		proxies2.add("190.9.11.1");
		proxies2.add("89.2.1.13");
		proxies2.add("64.200.1.20");
		proxies2.add("10.1.10.15");

		String originAddress3 = "193.176.1.4";
		LinkedList<String> proxies3 = new LinkedList<>();
		proxies3.add("64.200.1.20");
		proxies3.add("10.1.10.15");

		String originAddress4 = "77.8.100.30";
		LinkedList<String> proxies4 = new LinkedList<>();
		proxies4.add("109.1.20.3");
		proxies4.add("64.200.1.20");
		proxies4.add("68.35.10.101");
		proxies4.add("10.1.10.13");

		List<String> origins = new ArrayList<>();
		origins.add(originAddress1);
		origins.add(originAddress2);
		origins.add(originAddress3);
		origins.add(originAddress4);

		List<LinkedList<String>> proxyList = new ArrayList<>();
		proxyList.add(proxies1);
		proxyList.add(proxies2);
		proxyList.add(proxies3);
		proxyList.add(proxies4);

		EfficientProxyChainServiceImpl efficientProxyChainImpl = new EfficientProxyChainServiceImpl();

		for (LinkedList<String> proxies : proxyList) {
			efficientProxyChainImpl.findEfficientProxies(new LinkedList<>(proxies));
		}
		
		System.out.println("Efficient Proxy Chain test: ");
		efficientProxyChainImpl.alert(new LinkedList<>(proxies4), originAddress4, new Gson());
		
		assertEquals("[64.200.1.20]", efficientProxyChainImpl.getInefficientAddress().toString());
		assertEquals("[77.8.100.30, 109.1.20.3, 64.200.1.20, 68.35.10.101, 10.1.10.13]", efficientProxyChainImpl.getProxyChain().toString());
		assertEquals("[[77.8.100.30, 109.1.20.3, 64.200.1.20, 10.1.10.16], [77.8.100.30, 109.1.20.3, 64.200.1.20, 10.1.10.15]]", 
				efficientProxyChainImpl.getEfficientProxyChain().toString());

	}

}
