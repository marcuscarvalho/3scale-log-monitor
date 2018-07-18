package com.logmonitor.http.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public void log(Stats stats) {
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
	}

	public Stats calculateStats() {

		stats.setHits((stats.getGet() + stats.getPost()));
		calculateMostUsedProxyHits();
		calculatePercentile();
		stats.setTimestamp(System.currentTimeMillis());

		return stats;
	}

	public void addBadLine() {
		stats.setBadLines((stats.getBadLines() + 1));
	}

	private void calculateMostUsedProxyHits() {

		if (proxyHits != null && !proxyHits.isEmpty()) {
			String mostUsedProxy = proxyHits.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
			Integer mostUsedProxyHits = proxyHits.entrySet().stream().max(Map.Entry.comparingByValue()).get()
					.getValue();

			stats.setMostUsedProxy(mostUsedProxy);
			stats.setMostUsedProxyHits(mostUsedProxyHits);
		}
	}

	private void calculatePercentile() {

		if (responseTimes != null && !responseTimes.isEmpty()) {
			double[] responseTimeArray = responseTimes.stream().mapToDouble(Double::doubleValue).toArray();
			double percentile = LogMath.calculatePercentile(responseTimeArray, PERCENTILE);
			stats.setP95(percentile);
		}
	}

}
