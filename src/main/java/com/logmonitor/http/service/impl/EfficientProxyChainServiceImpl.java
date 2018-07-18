package com.logmonitor.http.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.logmonitor.domain.EfficientProxyChain;
import com.logmonitor.domain.alert.EfficientProxyChainAlert;

public class EfficientProxyChainServiceImpl {
	
	private Map<String, EfficientProxyChain> proxyChains = new HashMap<>();

	public void alert(LinkedList<String> proxies, final String originAddress) {

		if (proxies == null || proxies.isEmpty()) {
			return;
		}

		LinkedList<String> proxyUnderTest = new LinkedList<>(proxies);

		LinkedList<String> proxyChain = new LinkedList<>();
		List<String> inefficientAddress = new LinkedList<>();
		List<List<String>> efficientProxyChain = new ArrayList<>();

		for (int j = 0; j < proxyUnderTest.size(); j++) {
			if (proxyChains.containsKey(proxyUnderTest.get(j))) {
				String key = proxyUnderTest.get(j);
				EfficientProxyChain pChain = proxyChains.get(key);
				LinkedList<String> existingProxyChain = pChain.getProxySize();
				if (existingProxyChain.size() < (proxyUnderTest.size() - (j + 1))) {
					// setting hole proxy chain
					proxyChain.push(originAddress);
					proxyChain.addAll(proxyUnderTest);

					// setting inefficient address
					inefficientAddress.add(key);

					for (String efficientProxy : pChain.getEfficientProxies()) {
						List<String> efficientProxies = new ArrayList<>();

						for (String string : proxyChain) {
							efficientProxies.add(string);
							if (string.equals(key))
								break;
						}
						efficientProxies.add(efficientProxy);

						// setting efficient proxy chains
						efficientProxyChain.add(efficientProxies);
					}
				}
			}
		}

		if (!inefficientAddress.isEmpty()) {
			EfficientProxyChainAlert efficientProxyChainAlert = new EfficientProxyChainAlert();
			efficientProxyChainAlert.setTimestamp(System.currentTimeMillis());
			efficientProxyChainAlert.setProxyChain(proxyChain);
			efficientProxyChainAlert.setInefficientAddresses(inefficientAddress);
			efficientProxyChainAlert.setEfficientProxyChains(efficientProxyChain);

			Gson gson = new Gson();
			String json = gson.toJson(efficientProxyChainAlert);
			System.out.println(json);
		}
	}

	public void checkEfficientProxies(LinkedList<String> proxies) {

		if (proxies == null || proxies.isEmpty()) {
			return;
		}

		LinkedList<String> copyObj = new LinkedList<>(proxies);

		for (int j = 0; j < copyObj.size(); j++) {
			String pop = copyObj.pop();
			if (proxyChains.get(pop) == null) {
				EfficientProxyChain efficientProxyChain = new EfficientProxyChain(new LinkedList<>(copyObj),
						new LinkedList<>(copyObj));
				proxyChains.put(pop, efficientProxyChain);

			} else if (proxyChains.get(pop).getProxySize().size() > copyObj.size()) {
				EfficientProxyChain efficientProxyChain = new EfficientProxyChain(new LinkedList<>(copyObj),
						new LinkedList<>(copyObj));
				proxyChains.put(pop, efficientProxyChain); // efficient proxy chain

			} else if (proxyChains.get(pop).getProxySize().size() == copyObj.size()) {
				EfficientProxyChain current = proxyChains.get(pop);
				current.getEfficientProxies().addAll(copyObj); // another efficient proxy chain same size

			}
		}
	}

}
