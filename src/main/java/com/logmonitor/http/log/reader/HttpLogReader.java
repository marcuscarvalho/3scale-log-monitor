package com.logmonitor.http.log.reader;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.logmonitor.domain.EfficientProxyChain;
import com.logmonitor.domain.Stats;
import com.logmonitor.domain.alert.EfficientProxyChainAlert;
import com.logmonitor.http.log.HttpLog;
import com.logmonitor.http.log.exception.LogParseException;
import com.logmonitor.http.log.utils.HttpLogParser;
import com.logmonitor.http.service.impl.EfficientProxyChainServiceImpl;
import com.logmonitor.http.service.impl.StatsServiceImpl;
import com.logmonitor.http.service.impl.TrafficThresholdServiceImpl;

/**
 * 
 * @author Marcus Carvalho
 *
 */
public class HttpLogReader {
	
	private final static long LOG_MONITOR_TIMEOUT = 10000L; // 10 seconds
	
	private StatsServiceImpl statsService = new StatsServiceImpl();
	private TrafficThresholdServiceImpl trafficService = new TrafficThresholdServiceImpl();
	private EfficientProxyChainServiceImpl efficientProxyChainService = new EfficientProxyChainServiceImpl();
	
	public void readFile(String filePath, final int trafficThreshold) throws Exception {

		if (filePath == null)
			throw new IllegalArgumentException("file path cannot be null.");

		File file = new File(filePath);
		RandomAccessFile reader = null;
		try {
			reader = new RandomAccessFile(file, "r");
			// point to the end of file
			reader.seek(file.length());
		} catch (FileNotFoundException e) {
			// TODO File not found
			e.printStackTrace();
			throw new Exception("Log file does not exist, please check the file path passed as argument.");
		} catch (IOException e) {
			// TODO if seek pos is less than 0 or if an I/O error occurs.
			e.printStackTrace();
		}

		boolean shutdown = false;
		boolean thresholdViolation = false;

		long currentTimeMillis = System.currentTimeMillis();
		long previousTimeMillis = currentTimeMillis;
		
		LinkedList<Long> requestTime = new LinkedList<>();
		Map<Long, Integer> totalRequestPerTime = new HashMap<>();
		
		while (!shutdown) {
			
			if ((currentTimeMillis - previousTimeMillis) >= LOG_MONITOR_TIMEOUT) {
				previousTimeMillis = currentTimeMillis;
				Stats stats = statsService.calculateStats();
				statsService.log(stats);
				statsService = new StatsServiceImpl();
			}

			String line = null;
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO error reading log line from file
				e.printStackTrace();
			}

			if (line == null) {
				Thread.sleep(1000L);
				thresholdViolation = trafficService.alert(trafficThreshold, thresholdViolation, totalRequestPerTime);
				
			} else {

				HttpLog httpLog;
				try {
					httpLog = HttpLogParser.parse(line);
					statsService.populateStats(httpLog);
				} catch (LogParseException e) {
					// TODO: log in debug only
					statsService.addBadLine();
					continue;
				} catch (Exception e) {
					// TODO: log in error
					statsService.addBadLine();
					continue;
				}
				
				trafficService.calculateTraffic(requestTime, totalRequestPerTime);
				
				LinkedList<String> proxies = httpLog.getProxies();
				final String originAddress = httpLog.getOriginAddress();
				
				// calculate inefficient proxy chain
				efficientProxyChainService.checkEfficientProxies(proxies);
				// inefficient proxy chain alert 
				efficientProxyChainService.alert(proxies, originAddress);
			}

			currentTimeMillis = System.currentTimeMillis();
		}

		if (shutdown) {
			reader.close();
		}
	}

}
