package com.logmonitor.http.service;

import com.google.gson.Gson;

/**
 * Monitor the traffic limit and alert when it
 * crosses the threshold configured.
 * 
 * @author Marcus Carvalho
 *
 */
public interface TrafficThresholdService {

	int countRequests();

	void calculateTraffic();

	long updateTraffic();

	void alert(int trafficThreshold, Gson gson);

}
