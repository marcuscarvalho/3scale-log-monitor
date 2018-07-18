package com.logmonitor.domain.alert;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.logmonitor.domain.EfficientProxyChain;

public class EfficientProxyChainAlertTest {
	
	@Test
	public void testTemp() {

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

		Map<String, EfficientProxyChain> proxyChains = new HashMap<>();

		for (int i = 0; i < 4; i++) {
			
			LinkedList<String> proxies = proxyList.get(i);
			LinkedList<String> copyObj = new LinkedList<>(proxies);
			
			for (int j = 0; j < copyObj.size(); j++) {
				String pop = copyObj.pop();
				if (proxyChains.get(pop) == null) {
					EfficientProxyChain efficientProxyChain = new EfficientProxyChain(new LinkedList<>(copyObj), new LinkedList<>(copyObj));
					proxyChains.put(pop, efficientProxyChain);
				
				} else if (proxyChains.get(pop).getProxySize().size() > copyObj.size()) {
					EfficientProxyChain efficientProxyChain = new EfficientProxyChain(new LinkedList<>(copyObj), new LinkedList<>(copyObj));
					proxyChains.put(pop, efficientProxyChain); // efficient proxy chain
				
				} else if (proxyChains.get(pop).getProxySize().size() == copyObj.size()) { 
					EfficientProxyChain current = proxyChains.get(pop);
					current.getEfficientProxies().addAll(copyObj); // another efficient proxy chain same size
					 
				}
			}
		}

		LinkedList<String> proxyUnderTest = new LinkedList<>(proxies4);

		for (int j = 0; j < proxyUnderTest.size(); j++) {
			if (proxyChains.containsKey(proxyUnderTest.get(j))) {
				String key = proxyUnderTest.get(j);
				EfficientProxyChain efficientProxyChain = proxyChains.get(key);
				LinkedList<String> existingProxyChain = efficientProxyChain.getProxySize();
				if (existingProxyChain.size() < (proxyUnderTest.size() - (j + 1))) {
					LinkedList<String> proxyChain = new LinkedList<>(proxyUnderTest);
					System.out.println("proxy_chain: ");
					proxyChain.push(originAddress4);
					System.out.println(proxyChain);
					assertEquals("[77.8.100.30, 109.1.20.3, 64.200.1.20, 68.35.10.101, 10.1.10.13]", proxyChain.toString());
					
					System.out.println("inefficient_addresses: ");
					System.out.println(key);
					assertEquals("64.200.1.20", key);
					
					System.out.println("efficient_proxy_chains: ");
					List<List<String>> proxiesOutput = new ArrayList<>();
					
					for (String efficientProxy : efficientProxyChain.getEfficientProxies()) {
						List<String> efficientProxies = new ArrayList<>();
						
						for (String string : proxyChain) {
							efficientProxies.add(string);
							if (string.equals(key)) break;
						}
						efficientProxies.add(efficientProxy);
						proxiesOutput.add(efficientProxies);
					}
					
					System.out.println(proxiesOutput);
					assertEquals(proxiesOutput.toString(), "[[77.8.100.30, 109.1.20.3, 64.200.1.20, 10.1.10.16], [77.8.100.30, 109.1.20.3, 64.200.1.20, 10.1.10.15]]");
				}

			}
		}
	}

//	@Test
	public void test() {

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

		Map<String, LinkedList<String>> proxyChains = new HashMap<>();

		for (int i = 0; i < 4; i++) {
			
			LinkedList<String> proxies = proxyList.get(i);

			LinkedList<String> copyObj = new LinkedList<>(proxies);
			
			
			for (int j = 0; j < copyObj.size(); j++) {
				String pop = copyObj.pop();
				if (proxyChains.get(pop) == null) {
					proxyChains.put(pop, new LinkedList<>(copyObj));
				
				} else if (proxyChains.get(pop).size() > copyObj.size()) {
					proxyChains.put(pop, copyObj); // efficient proxy chain
				
				} else if (proxyChains.get(pop).size() == copyObj.size()) { 
					 // another efficient proxy chain
				}
			}
		}

		LinkedList<String> proxyUnderTest = new LinkedList<>(proxies4);

		for (int j = 0; j < proxyUnderTest.size(); j++) {
			if (proxyChains.containsKey(proxyUnderTest.get(j))) {
				String key = proxyUnderTest.get(j);
				LinkedList<String> existingProxyChain = proxyChains.get(key);
				if (existingProxyChain.size() < (proxyUnderTest.size() - (j + 1))) {
					LinkedList<String> proxyChain = new LinkedList<>(proxyUnderTest);
					System.out.println("proxy_chain: ");
					proxyChain.push(originAddress4);
					System.out.println(proxyChain);
					System.out.println("inefficient_addresses: ");
					System.out.println(key);
					System.out.println("efficient_proxy_chains: ");
					System.out.println(existingProxyChain);

				}

			}
		}
	}

}
