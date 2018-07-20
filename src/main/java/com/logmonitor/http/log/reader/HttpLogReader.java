package com.logmonitor.http.log.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.logmonitor.http.log.HttpLog;
import com.logmonitor.http.log.exception.LogParseException;
import com.logmonitor.http.log.utils.HttpLogParser;
import com.logmonitor.http.service.EfficientProxyChainService;
import com.logmonitor.http.service.StatsService;
import com.logmonitor.http.service.TrafficThresholdService;
import com.logmonitor.http.service.impl.EfficientProxyChainServiceImpl;
import com.logmonitor.http.service.impl.StatsServiceImpl;
import com.logmonitor.http.service.impl.TrafficThresholdServiceImpl;

/**
 * Read HTTP Access Log from a specified path and print into the 
 * console some information e.g. server status, traffic threshold violation
 * and inefficient proxy chain when detected.
 * 
 * @author Marcus Carvalho
 *
 */
public class HttpLogReader {
	
	private static final Logger logger = Logger.getLogger(HttpLogReader.class);
	private static final long LOG_MONITOR_TIMEOUT = 10000L; // 10 seconds
	
	private Gson gson = new Gson();
	private StatsService statsService = new StatsServiceImpl();
	private TrafficThresholdService trafficThresholdService = new TrafficThresholdServiceImpl();
	private EfficientProxyChainService efficientProxyChainService = new EfficientProxyChainServiceImpl();
	private boolean shutdown = false;
	
	/**
	 * Read the log file to start monitoring it for http stats, 
	 * traffic threshold and inefficient proxy chains alerts.
	 *  
	 * @param filePath Log File path
	 * @param trafficThreshold Traffic Threshold value
	 * @throws Exception
	 */
	public void readFile(final String filePath, final int trafficThreshold) throws Exception {

		if (filePath == null)
			throw new IllegalArgumentException("file path cannot be null.");

		RandomAccessFile reader = createRandomAccessFile(filePath);
		

		// start a program shutdown hook within another thread
		addShutdownHook();

		long currentTimeMillis = System.currentTimeMillis();
		long previousTimeMillis = currentTimeMillis;
		
		while (!shutdown) {
			
			previousTimeMillis = printStats(currentTimeMillis, previousTimeMillis);

			String line = null;
			try {
				line = reader.readLine();
			} catch (IOException e) {
				logger.error(e);
			}

			// Whenever the traffic conditions are detected above the threshold, output a message describing an alert
			trafficThresholdService.alert(trafficThreshold, gson);
			
			if (line == null) {
				// Sleep thread for 1 second to avoid CPU high utilization. 
				// Another implementation would be implementing a listener to notify when a new log line arrives.
				Thread.sleep(1000L); 
				trafficThresholdService.updateTraffic();
				
			} else {
				HttpLog httpLog;
				try {
					httpLog = HttpLogParser.parse(line);
				} catch (LogParseException e) {
					logger.debug("[bad line] ", e);
					statsService.addBadLine();
					continue;
				} catch (Exception e) {
					logger.debug("[bad line] ", e);
					statsService.addBadLine();
					continue;
				}
				
				// populate stats object with a new request
				statsService.populate(httpLog);
				
				// calculate traffic for threshold alerts
				trafficThresholdService.calculateTraffic();
				
				LinkedList<String> proxies = httpLog.getProxies();
				final String originAddress = httpLog.getOriginAddress();
				
				// calculate inefficient proxy chain
				efficientProxyChainService.findEfficientProxies(proxies);
				
				// raise an alert when a inefficient proxy chain is observed
				efficientProxyChainService.alert(proxies, originAddress, gson);
			}

			currentTimeMillis = System.currentTimeMillis();
		}
		
		if (shutdown && reader != null) {
			reader.close();
		}
	}
	
	/**
	 * Create Random access file reader to navigate within the log file.
	 * 
	 * @param filePath
	 * @return RandomAccessFile
	 * @throws Exception
	 */
	protected RandomAccessFile createRandomAccessFile(final String filePath) throws Exception {
		File file = new File(filePath);
		RandomAccessFile reader = null;
		try {
			reader = new RandomAccessFile(file, "r");
			// point to the end of file to discard old lines 
			reader.seek(file.length());
		} catch (FileNotFoundException e) {
			logger.error(e);
			throw new Exception("Log file does not exist, please check the file path passed as argument.");
		} catch (IOException e) {
			logger.error(e);
		}
		return reader;
	}

	/**
	 * Check if the time has passed and print server stats
	 * 
	 * @param currentTimeMillis
	 * @param previousTimeMillis
	 * @return long
	 */
	private long printStats(long currentTimeMillis, long previousTimeMillis) {
		if ((currentTimeMillis - previousTimeMillis) >= LOG_MONITOR_TIMEOUT) {
			previousTimeMillis = currentTimeMillis;
			statsService.calculate();
			statsService.print(gson);
			statsService = new StatsServiceImpl();
		}
		return previousTimeMillis;
	}

	/**
	 * Add a program shutdown hook within another thread 
	 * to monitor ctrl + c or when the process is killed.
	 */
	private void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// log info to help understanding why the process 
				// suddenly  stopped
				logger.info("aborting program by shutdown hook");
				statsService.print(gson);
				shutdown = true;
			}
		});
	}

}
