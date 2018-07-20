package com.logmonitor.http.log.utils;

import java.util.Arrays;

/*
 * Calculate 95th percentile for a given array of doubles.
 * 
 * To double check, I have tested it using the calculator below
 * 
 * https://www.easycalculation.com/statistics/95th-percentile.php
 * 
 * And the Math picture (percentile-calculation.jpg) in attach.
 */
public class Percentile {
	
	/**
	 * Calculate the percentile of an array of double values
	 * 
	 * @param latencies
	 * @param percentile
	 * @return double
	 */
	public static double calculate(double[] latencies, double percentile) {
		Arrays.sort(latencies);
		int index = (int) Math.ceil(((double) percentile / (double) 100) * (double) latencies.length);
		return latencies[index-1];
	}

}
