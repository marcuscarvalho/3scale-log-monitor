package com.logmonitor.http.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.gson.Gson;
import com.logmonitor.domain.alert.TrafficThresholdCrossedAlert;
import com.logmonitor.domain.types.ALERT_TYPE;
import com.logmonitor.http.service.TrafficThresholdService;

public class TrafficThresholdServiceImpl implements TrafficThresholdService {

	private final static int PAST_MINUTE_IN_SECONDS = 60;
	private LinkedList<Long> requestTime = new LinkedList<>();
	private Map<Long, Integer> totalRequestPerTime = new HashMap<>();
	private boolean thresholdViolation = false;
	
	@Override
	public int countRequests() {
		return totalRequestPerTime.values().stream().mapToInt(Number::intValue).sum();
	}
	
	@Override
	public void calculateTraffic() {
		long currentTimeInSeconds = updateTraffic();

		if (!requestTime.contains(currentTimeInSeconds)) {
			requestTime.add(currentTimeInSeconds);
			totalRequestPerTime.put(currentTimeInSeconds, 1);
		} else {
			totalRequestPerTime.put(currentTimeInSeconds, (totalRequestPerTime.get(currentTimeInSeconds) + 1));
		}
	}

	@Override
	public long updateTraffic() {
		long currentTimeInSeconds = System.currentTimeMillis() / 1000;

		if (!requestTime.isEmpty()) {
			for (int i = 0; i < requestTime.size(); i++) {
				if ((currentTimeInSeconds - requestTime.getFirst()) >= PAST_MINUTE_IN_SECONDS) {
					Long removedKey = requestTime.pollFirst();
					totalRequestPerTime.remove(removedKey);
				} else {
					break;
				}
			}
		}
		return currentTimeInSeconds;
	}

	@Override
	public void alert(final int trafficThreshold, Gson gson) {

		int totalTrafficPastMinute = totalRequestPerTime.values().stream().mapToInt(Number::intValue).sum();

		if (totalTrafficPastMinute > trafficThreshold) {
			thresholdViolation = true;

			TrafficThresholdCrossedAlert trafficThresholdCrossedAlert = new TrafficThresholdCrossedAlert();
			trafficThresholdCrossedAlert.setAlertType(ALERT_TYPE.TRAFFIC_ABOVE_THRESHOLD);
			trafficThresholdCrossedAlert.setTimestamp(System.currentTimeMillis());
			trafficThresholdCrossedAlert.setThreshold(trafficThreshold);
			trafficThresholdCrossedAlert.setCurrentValue(totalTrafficPastMinute);

			// Java object to JSON, and assign to a String
			String json = gson.toJson(trafficThresholdCrossedAlert);

			// print traffic threshold in json format to terminal
			System.out.println(json);

		} else if (thresholdViolation && totalTrafficPastMinute < trafficThreshold) {

			TrafficThresholdCrossedAlert trafficThresholdCrossedAlert = new TrafficThresholdCrossedAlert();
			trafficThresholdCrossedAlert.setAlertType(ALERT_TYPE.TRAFFIC_BELOW_THRESHOLD);
			trafficThresholdCrossedAlert.setTimestamp(System.currentTimeMillis());
			trafficThresholdCrossedAlert.setThreshold(trafficThreshold);
			trafficThresholdCrossedAlert.setCurrentValue(totalTrafficPastMinute);

			// Java object to JSON, and assign to a String
			String json = gson.toJson(trafficThresholdCrossedAlert);

			// print traffic threshold in json format to terminal
			System.out.println(json);

			thresholdViolation = false;
		}
	}

}
