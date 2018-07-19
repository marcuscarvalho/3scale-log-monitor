package com.logmonitor.http.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class StatsServiceImplTest {
	
	@Test
	public void test() {

		Map<String, Integer> proxyHits = new HashMap<>();
		proxyHits.put("10.200.200.200", 1);
		proxyHits.put("10.200.200.203", 3);
		proxyHits.put("10.200.200.254", 10);
		proxyHits.put("10.200.200.255", 10);
		proxyHits.put("10.200.200.100", 2);
		
		StatsServiceImpl statsServiceImpl = new StatsServiceImpl(proxyHits);
		statsServiceImpl.calculateMostUsedProxyHits();
		List<String> mostUsedProxies = statsServiceImpl.getStats().getMostUsedProxy();
		
		List<String> expectedAddresses = new ArrayList<>(); 
		expectedAddresses.add("10.200.200.254");
		expectedAddresses.add("10.200.200.255");
		
		Collections.sort(expectedAddresses);
		Collections.sort(mostUsedProxies);
		
		assertEquals(expectedAddresses, mostUsedProxies);
		
	}

}
