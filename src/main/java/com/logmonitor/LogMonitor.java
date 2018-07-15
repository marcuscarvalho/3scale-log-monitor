package com.logmonitor;

import com.logmonitor.http.log.reader.HttpLogReader;

/**
 * 
 * @author Marcus Carvalho
 *
 */
public class LogMonitor {

	public static void main(String[] args) throws Exception {
		HttpLogReader httpLogReader = new HttpLogReader();
		
		String filePath = null;
		int trafficThreshold = 0;

		try {
			filePath = args[0];
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new Exception("Log file path was not specified");
		}
		
		try {
			trafficThreshold  = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			throw new Exception("Traffic Threshold should be an integer number");
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new Exception("Traffic Threshold was not specified");
		}
		
		httpLogReader.readFile(filePath, trafficThreshold);
	}

}
