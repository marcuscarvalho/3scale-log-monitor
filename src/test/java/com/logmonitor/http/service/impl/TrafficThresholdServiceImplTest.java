package com.logmonitor.http.service.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TrafficThresholdServiceImplTest {

	 @Test
	public void test() throws InterruptedException {
		TrafficThresholdServiceImpl impl = new TrafficThresholdServiceImpl();

		int keepAlive = 73;
		int currentTimeInSeconds = 0;
		
		while (currentTimeInSeconds < keepAlive) {
			Thread.sleep(1000L);
			currentTimeInSeconds++;
			switch (currentTimeInSeconds) {
			case 5:
				for (int i = 1; i <= 1; i++) { 
					impl.calculateTraffic();
				}
				System.out.println("5 seconds passed - total requests: " + impl.countRequests());
				assertEquals(1, impl.countRequests());
				break;
			case 10:
				for (int i = 1; i <= 80; i++) {
					impl.calculateTraffic();
				}
				System.out.println("10 seconds passed - total requests: " + impl.countRequests());
				assertEquals(81, impl.countRequests());
				break;
			case 15:
				for (int i = 1; i <= 5; i++) {
					impl.calculateTraffic();
				}
				System.out.println("15 seconds passed - total requests: " + impl.countRequests());
				assertEquals(86, impl.countRequests());
				break;
			case 60:
				for (int i = 1; i <= 1; i++) {
					impl.calculateTraffic();
				}
				System.out.println("60 seconds passed - total requests: " + impl.countRequests());
				assertEquals(87, impl.countRequests());
				break;
			case 61:
				impl.updateTraffic();
				System.out.println("61 seconds passed - total requests: " + impl.countRequests());
				assertEquals(87, impl.countRequests());

				break;
			case 71:
				impl.updateTraffic();
				System.out.println("71 seconds passed - total requests: " + impl.countRequests());
				assertEquals(6, impl.countRequests());
				break;

			default:
				break;
			}

		}

	}

}
