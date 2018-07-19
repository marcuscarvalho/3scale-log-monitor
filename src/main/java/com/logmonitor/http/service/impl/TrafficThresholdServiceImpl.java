package com.logmonitor.http.service.impl;

import java.util.LinkedList;
import java.util.Map;

import com.google.gson.Gson;
import com.logmonitor.domain.alert.TrafficThresholdCrossedAlert;
import com.logmonitor.domain.types.ALERT_TYPE;

public class TrafficThresholdServiceImpl {

	private final static int PAST_MINUTE_IN_SECONDS = 60;
	private Gson gson = new Gson();
	
	public void calculateTraffic(LinkedList<Long> requestTime, Map<Long, Integer> totalRequestPerTime) {
		long currentTimeInSeconds = updateTraffic(requestTime, totalRequestPerTime);

		if (!requestTime.contains(currentTimeInSeconds)) {
			requestTime.add(currentTimeInSeconds);
			totalRequestPerTime.put(currentTimeInSeconds, 1);
		} else {
			totalRequestPerTime.put(currentTimeInSeconds, (totalRequestPerTime.get(currentTimeInSeconds) + 1));
		}
	}

	public long updateTraffic(LinkedList<Long> requestTime, Map<Long, Integer> totalRequestPerTime) {
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

	public boolean alert(final int trafficThreshold, boolean thresholdViolation,
			Map<Long, Integer> totalRequestPerTime) {

		int totalTrafficPastMinute = totalRequestPerTime.values().stream().mapToInt(Number::intValue).sum();

		if (totalTrafficPastMinute > trafficThreshold) {
			thresholdViolation = true;

			TrafficThresholdCrossedAlert trafficThresholdCrossedAlert = new TrafficThresholdCrossedAlert();
			trafficThresholdCrossedAlert.setAlertType(ALERT_TYPE.TRAFFIC_ABOVE_THRESHOLD);
			trafficThresholdCrossedAlert.setTimestamp(System.currentTimeMillis());
			trafficThresholdCrossedAlert.setThreshold(trafficThreshold);
			trafficThresholdCrossedAlert.setCurrentValue(totalTrafficPastMinute);

			// Java object to JSON, and assign to a String
			String trafficThresholdAlertJson = gson.toJson(trafficThresholdCrossedAlert);

			// print traffic threshold in json format to terminal
			System.out.println(trafficThresholdAlertJson);

		} else if (thresholdViolation && totalTrafficPastMinute < trafficThreshold) {

			TrafficThresholdCrossedAlert trafficThresholdCrossedAlert = new TrafficThresholdCrossedAlert();
			trafficThresholdCrossedAlert.setAlertType(ALERT_TYPE.TRAFFIC_BELOW_THRESHOLD);
			trafficThresholdCrossedAlert.setTimestamp(System.currentTimeMillis());
			trafficThresholdCrossedAlert.setThreshold(trafficThreshold);
			trafficThresholdCrossedAlert.setCurrentValue(totalTrafficPastMinute);

			// Java object to JSON, and assign to a String
			String trafficThresholdAlertJson = gson.toJson(trafficThresholdCrossedAlert);

			// print traffic threshold in json format to terminal
			System.out.println(trafficThresholdAlertJson);

			thresholdViolation = false;
		}
		return thresholdViolation;
	}

}
