package com.logmonitor.http.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.logmonitor.domain.Stats;

public class StatsServiceImplTest {
	
	@Test
	public void testMostUsedProxies() {
		Map<String, Integer> proxyHits = new HashMap<>();
		proxyHits.put("10.200.200.200", 1);
		proxyHits.put("10.200.200.203", 3);
		proxyHits.put("10.200.200.254", 10);
		proxyHits.put("10.200.200.255", 10);
		proxyHits.put("10.200.200.100", 2);
		
		Stats stats = new Stats();
		StatsServiceImpl statsServiceImpl = new StatsServiceImpl(stats, proxyHits, null);
		statsServiceImpl.calculateMostUsedProxyHits();
		List<String> mostUsedProxies = statsServiceImpl.getStats().getMostUsedProxy();
		
		List<String> expectedAddresses = new ArrayList<>(); 
		expectedAddresses.add("10.200.200.254");
		expectedAddresses.add("10.200.200.255");
		
		Collections.sort(expectedAddresses);
		Collections.sort(mostUsedProxies);
		
		assertEquals(expectedAddresses, mostUsedProxies);
	}
	
	/**
	 * https://www.easycalculation.com/statistics/95th-percentile.php
	 */
	@Test
	public void testCalculatePercentile() {
		List<Double> statsResponseTimes = new ArrayList<>();
		statsResponseTimes.add(3d);
		statsResponseTimes.add(6d);
		statsResponseTimes.add(7d);
		statsResponseTimes.add(8d);
		statsResponseTimes.add(9d);
		statsResponseTimes.add(10d);
		statsResponseTimes.add(13d);
		statsResponseTimes.add(15d);
		statsResponseTimes.add(16d);
		statsResponseTimes.add(20d);
		
		Stats stats = new Stats();
		StatsServiceImpl statsServiceImpl = new StatsServiceImpl(stats, null, statsResponseTimes);
		statsServiceImpl.calculatePercentile();
		
		assertEquals(20d, stats.getP95(), 0.00);
	}

}
