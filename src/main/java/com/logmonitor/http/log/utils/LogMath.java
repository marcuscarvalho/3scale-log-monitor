package com.logmonitor.http.log.utils;

import java.util.Arrays;

public class LogMath {

	public static double calculatePercentile(double[] latencies, double percentile) {
		Arrays.sort(latencies);
		double p = ((double) percentile / (double) 100);

		double k = (latencies.length - 1) * p;
		double f = Math.floor(k);
		double c = Math.ceil(k);
		if (f == c) {
			return latencies[(int) (k)];
		}

		double ck = c - k;
		double kf = k - f;
		double d0 = latencies[(int) (f)] * ck;
		double d1 = latencies[(int) (c)] * kf;
		return d0 + d1;
	}

}
