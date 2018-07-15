package com.logmonitor;

import java.io.IOException;

import com.logmonitor.http.log.reader.HttpLogReader;

/**
 * 
 * @author Marcus Carvalho
 *
 */
public class LogMonitor {

	public static void main(String[] args) throws InterruptedException, IOException {
		HttpLogReader httpLogReader = new HttpLogReader();
		
		String filePath = args[0];
		int trafficThreshold = Integer.parseInt(args[1]);
		
		httpLogReader.readFile(filePath, trafficThreshold);
		
//		httpLogReader.readFile("httpaccess.log", 60);
	}

}
