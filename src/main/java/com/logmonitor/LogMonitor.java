package com.logmonitor;

import org.apache.log4j.Logger;

import com.logmonitor.http.log.reader.HttpLogReader;

/**
 * 
 * @author Marcus Carvalho
 *
 */
public class LogMonitor {
	
	private static final Logger logger = Logger.getLogger(LogMonitor.class);

	/**
	 * This is the entry point to start monitoring an http access log file.
	 * param1: Log file path 
	 * param2: Traffic threshold value 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		HttpLogReader httpLogReader = new HttpLogReader();
		
		final String filePath;
		final int trafficThreshold;

		try {
			filePath = args[0];
		} catch (ArrayIndexOutOfBoundsException ex) {
			logger.error(ex);
			throw new Exception("Log file path was not specified");
		}
		
		try {
			trafficThreshold  = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			logger.error(ex);
			throw new Exception("Traffic Threshold should be an integer number");
		} catch (ArrayIndexOutOfBoundsException ex) {
			logger.error(ex);
			throw new Exception("Traffic Threshold value was not specified");
		}
		
		httpLogReader.readFile(filePath, trafficThreshold);
	}

}
