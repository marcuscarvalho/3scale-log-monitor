package com.logmonitor.http.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

public class TrafficThresholdServiceImplTest {

	 @Test
	public void test() throws InterruptedException {
		LinkedList<Long> requestTime = new LinkedList<>();
		Map<Long, Integer> totalRequestPerTime = new HashMap<>();
		TrafficThresholdServiceImpl trafficThresholdServiceImpl = new TrafficThresholdServiceImpl();

		int keepAlive = 125;
		int currentTimeInSeconds = 0;
		
		while (currentTimeInSeconds < keepAlive) {
			Thread.sleep(1000L);
			currentTimeInSeconds++;
			switch (currentTimeInSeconds) {
			case 5:
				for (int i = 1; i <= 1; i++) {
					trafficThresholdServiceImpl.calculateTraffic(requestTime, totalRequestPerTime);
				}
				System.out.println("5 segundos total request 1: " + totalRequestPerTime);
				break;
			case 10:
				for (int i = 1; i <= 80; i++) {
					trafficThresholdServiceImpl.calculateTraffic(requestTime, totalRequestPerTime);
				}
				System.out.println("10 segundos total request 81: " + totalRequestPerTime);
				break;
			case 15:
				for (int i = 1; i <= 5; i++) {
					trafficThresholdServiceImpl.calculateTraffic(requestTime, totalRequestPerTime);
				}
				System.out.println("15 segundos total request 86: " + totalRequestPerTime);
				break;
			case 60:
				for (int i = 1; i <= 1; i++) {
					trafficThresholdServiceImpl.calculateTraffic(requestTime, totalRequestPerTime);
				}
				System.out.println("60 segundos total request 87: " + totalRequestPerTime);
				break;
			case 61:
				trafficThresholdServiceImpl.updateTraffic(requestTime, totalRequestPerTime);
				System.out.println("61 segundos total request 87: " + totalRequestPerTime);

				break;
			case 71:
				trafficThresholdServiceImpl.updateTraffic(requestTime, totalRequestPerTime);
				System.out.println("71 segundos total request 6: " + totalRequestPerTime);
				break;
			case 120:
				trafficThresholdServiceImpl.updateTraffic(requestTime, totalRequestPerTime);
				System.out.println("120 segundos total request 1: " + totalRequestPerTime);
				break;
			case 121:
				trafficThresholdServiceImpl.updateTraffic(requestTime, totalRequestPerTime);
				System.out.println("120 segundos total request 0: " + totalRequestPerTime);
				break;

			default:
				break;
			}

		}

	}

}
