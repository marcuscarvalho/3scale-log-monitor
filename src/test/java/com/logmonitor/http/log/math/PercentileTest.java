package com.logmonitor.http.log.math;

import static org.junit.Assert.*;

import org.junit.Test;

import com.logmonitor.http.log.utils.Percentile;

public class PercentileTest {

	@Test
	public void test25thPercentile() {
		double[] latencies = {3d, 6d, 7d, 8d, 9d, 10d, 13d, 15d, 16d, 20d}; 
//		double[] values = statsResponseTimes.stream().mapToDouble(d -> d).toArray();
		double percentile = Percentile.calculate(latencies, 25);
		assertEquals(7d, percentile, 0.00);
	}
	
	@Test
	public void test50thPercentile() {
		double[] latencies = {3d, 6d, 7d, 8d, 9d, 10d, 13d, 15d, 16d, 20d}; 
		double percentile = Percentile.calculate(latencies, 50);
		assertEquals(9d, percentile, 0.00);
	}
	
	@Test
	public void test75thPercentile() {
		double[] latencies = {3d, 6d, 7d, 8d, 9d, 10d, 13d, 15d, 16d, 20d}; 
		double percentile = Percentile.calculate(latencies, 75);
		assertEquals(15d, percentile, 0.00);
	}
	
	@Test
	public void test95thPercentile() {
		double[] latencies = {3d, 6d, 7d, 8d, 9d, 10d, 13d, 15d, 16d, 20d}; 
		double percentile = Percentile.calculate(latencies, 95);
		assertEquals(20d, percentile, 0.00);
	}
	
	@Test
	public void test100thPercentile() {
		double[] latencies = {3d, 6d, 7d, 8d, 9d, 10d, 13d, 15d, 16d, 20d}; 
		double percentile = Percentile.calculate(latencies, 100);
		assertEquals(20d, percentile, 0.00);
	}

}
