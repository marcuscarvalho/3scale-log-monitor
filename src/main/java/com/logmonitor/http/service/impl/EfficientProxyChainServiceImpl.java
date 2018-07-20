package com.logmonitor.http.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.logmonitor.domain.EfficientProxyChain;
import com.logmonitor.domain.alert.EfficientProxyChainAlert;
import com.logmonitor.http.service.EfficientProxyChainService;

public class EfficientProxyChainServiceImpl implements EfficientProxyChainService {

	private LinkedList<String> proxyChain = new LinkedList<>();
	private List<String> inefficientAddress = new LinkedList<>();
	private List<List<String>> efficientProxyChain = new ArrayList<>();
	private Map<String, EfficientProxyChain> proxyChainMap = new HashMap<>();

	@Override
	public void alert(final LinkedList<String> proxies, final String originAddress, Gson gson) {

		if (proxies == null || proxies.isEmpty()) {
			return;
		}

		LinkedList<String> proxiesCopy = new LinkedList<>(proxies);

		for (int j = 0; j < proxiesCopy.size(); j++) {
			if (proxyChainMap.containsKey(proxiesCopy.get(j))) {
				String key = proxiesCopy.get(j);
				EfficientProxyChain pChain = proxyChainMap.get(key);
				if (pChain.getPreviousSize() < (proxiesCopy.size() - (j + 1))) {
					// setting hole proxy chain
					proxyChain.push(originAddress);
					proxyChain.addAll(proxiesCopy);

					// setting inefficient address
					inefficientAddress.add(key);

					for (String efficientProxy : pChain.getProxies()) {
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

		printJson(gson, proxyChain, inefficientAddress, efficientProxyChain);
	}

	@Override
	public void findEfficientProxies(final LinkedList<String> proxies) {
		if (proxies == null || proxies.isEmpty()) {
			return;
		}

		LinkedList<String> safeCopy = new LinkedList<>(proxies);
		for (int j = 0; j < safeCopy.size(); j++) {
			populate(safeCopy);
		}
	}

	protected void populate(final LinkedList<String> proxies) {
		String proxy = proxies.pop();
		int currentSize = new LinkedList<>(proxies).size();
		
		if (proxyChainMap.get(proxy) == null) {
			EfficientProxyChain proxyChain = new EfficientProxyChain(currentSize, new LinkedList<>(proxies));
			proxyChainMap.put(proxy, proxyChain);

		} else if (proxyChainMap.get(proxy).getPreviousSize() > proxies.size()) {
			EfficientProxyChain efficientProxyChain = new EfficientProxyChain(currentSize, new LinkedList<>(proxies));
			proxyChainMap.put(proxy, efficientProxyChain); // efficient proxy chain observed

		} else if (proxyChainMap.get(proxy).getPreviousSize() == proxies.size()) {
			proxyChainMap.get(proxy).getProxies().addAll(proxies); // another efficient proxy chain with the same size
		}
	}

	
	public LinkedList<String> getProxyChain() {
		return proxyChain;
	}

	public List<String> getInefficientAddress() {
		return inefficientAddress;
	}

	public List<List<String>> getEfficientProxyChain() {
		return efficientProxyChain;
	}

	private void printJson(Gson gson, LinkedList<String> currentChain, List<String> inefficientAddress,
			List<List<String>> efficientChain) {
		if (!inefficientAddress.isEmpty()) {
			EfficientProxyChainAlert efficientProxyChainAlert = new EfficientProxyChainAlert();
			efficientProxyChainAlert.setTimestamp(System.currentTimeMillis());
			efficientProxyChainAlert.setProxyChain(currentChain);
			efficientProxyChainAlert.setInefficientAddresses(inefficientAddress);
			efficientProxyChainAlert.setEfficientProxyChains(efficientChain);

			String json = gson.toJson(efficientProxyChainAlert);
			System.out.println(json);
		}
	}

}
