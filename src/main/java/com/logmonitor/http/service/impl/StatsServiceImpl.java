package com.logmonitor.http.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.logmonitor.domain.Stats;
import com.logmonitor.http.log.HttpLog;
import com.logmonitor.http.log.utils.Percentile;
import com.logmonitor.http.service.StatsService;

/**
 * Service class responsible to process http server stats.
 * 
 * @author Marcus Carvalho
 *
 */
public class StatsServiceImpl implements StatsService {

	private static final double PERCENTILE = 95d; // 95th percentile

	private Stats stats = new Stats();
	private Map<String, Integer> statsProxyHits = new HashMap<>();
	private List<Double> statsResponseTimes = new ArrayList<>();
	
	public StatsServiceImpl() {}
	
	public StatsServiceImpl(Stats stats, Map<String, Integer> statsProxyHits, List<Double> statsResponseTimes) {
		this.stats = stats;
		this.statsProxyHits = statsProxyHits;
		this.statsResponseTimes = statsResponseTimes;
	}

	@Override
	public void print(Gson gson) {
		// Java object to JSON, and assign to a String
		String statsJson = gson.toJson(stats);

		// print stats to terminal console
		System.out.println(statsJson);
	}

	@Override
	public void populate(HttpLog httpLog) {

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
				if (statsProxyHits.get(proxy) != null) {
					proxyHitsCount += statsProxyHits.get(proxy);
				}
				statsProxyHits.put(proxy, proxyHitsCount);
			}
		}

		// Adding request response time to array list to calculate percentile
		statsResponseTimes.add(httpLog.getResponseTimeInSeconds());
	}

	@Override
	public void calculate() {
		stats.setHits((stats.getGet() + stats.getPost()));
		calculateMostUsedProxyHits();
		calculatePercentile();
		stats.setTimestamp(System.currentTimeMillis());
	}

	@Override
	public void addBadLine() {
		stats.setBadLines((stats.getBadLines() + 1));
	}
	
	@Override
	public Stats getStats() {
		return stats;
	}

	protected void calculateMostUsedProxyHits() {

		if (statsProxyHits != null && !statsProxyHits.isEmpty()) {
			List<String> mostUsedProxies = new ArrayList<>();
			Integer maxValue = statsProxyHits.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
			
			for (Entry<String, Integer> entry : statsProxyHits.entrySet()) {
				if (maxValue.equals(entry.getValue())) {
					mostUsedProxies.add(entry.getKey());
				}
			}
			
			stats.setMostUsedProxy(mostUsedProxies);
			stats.setMostUsedProxyHits(maxValue);
		}
	}
	
	protected void calculatePercentile() {
		if (statsResponseTimes != null && !statsResponseTimes.isEmpty()) {
			double[] values = statsResponseTimes.stream().mapToDouble(d -> d).toArray();
			double percentile = Percentile.calculate(values, PERCENTILE);
			stats.setP95(percentile);
		}
	}

}
