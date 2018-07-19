package com.logmonitor.http.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.logmonitor.domain.Stats;
import com.logmonitor.http.log.HttpLog;
import com.logmonitor.http.log.utils.LogMath;

public class StatsServiceImpl {

	private static final double PERCENTILE = 95d; // 95th percentile

	private Gson gson = new Gson();
	private Stats stats = new Stats();
	private Map<String, Integer> proxyHits = new HashMap<>();
	private List<Double> responseTimes = new ArrayList<>();
	
	public StatsServiceImpl() {
		
	}
	
	public StatsServiceImpl(Map<String, Integer> proxyHits) {
		this.proxyHits = proxyHits;
	}

	public void printStats() {
		// Java object to JSON, and assign to a String
		String statsJson = gson.toJson(stats);

		// print stats to terminal console
		System.out.println(statsJson);
	}

	public void populateStats(HttpLog httpLog) {

		if (httpLog.getHttpMethod().equals("GET")) {
			stats.setGet((stats.getGet() + 1));
		} else if (httpLog.getHttpMethod().equals("POST")) {
			stats.setPost((stats.getPost() + 1));
		}

		if (httpLog.getProxies() != null && httpLog.getProxies().size() > 0) {
			stats.setForwardedHits((stats.getForwardedHits() + 1));
		}

		if (httpLog != null && httpLog.getProxies() != null) {
			for (String proxy : httpLog.getProxies()) {
				int proxyHitsCount = 1;
				if (proxyHits.get(proxy) != null) {
					proxyHitsCount += proxyHits.get(proxy);
				}
				proxyHits.put(proxy, proxyHitsCount);
			}
		}

		// Adding request response time to array list to calculate percentile
		responseTimes.add(httpLog.getResponseTimeInSeconds());
		stats.setTimestamp(System.currentTimeMillis());
	}

	public void calculateStats() {
		stats.setHits((stats.getGet() + stats.getPost()));
		calculateMostUsedProxyHits();
		calculatePercentile();
	}

	public void addBadLine() {
		stats.setBadLines((stats.getBadLines() + 1));
	}

	protected void calculateMostUsedProxyHits() {

		if (proxyHits != null && !proxyHits.isEmpty()) {
			List<String> mostUsedProxies = new ArrayList<>();
			Integer maxValue = proxyHits.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
			
			for (Entry<String, Integer> entry : proxyHits.entrySet()) {
				if (maxValue.equals(entry.getValue())) {
					mostUsedProxies.add(entry.getKey());
				}
			}
			
			stats.setMostUsedProxy(mostUsedProxies);
			stats.setMostUsedProxyHits(maxValue);
		}
	}
	
	public Stats getStats() {
		return stats;
	}

	private void calculatePercentile() {

		if (responseTimes != null && !responseTimes.isEmpty()) {
			double[] responseTimeArray = responseTimes.stream().mapToDouble(Double::doubleValue).toArray();
			double percentile = LogMath.calculatePercentile(responseTimeArray, PERCENTILE);
			stats.setP95(percentile);
		}
	}

}
