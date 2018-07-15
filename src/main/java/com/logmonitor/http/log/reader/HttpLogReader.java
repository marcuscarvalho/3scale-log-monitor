package com.logmonitor.http.log.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.logmonitor.domain.Stats;
import com.logmonitor.http.log.HttpLog;
import com.logmonitor.http.log.utils.HttpLogParser;

/**
 * 
 * @author Marcus Carvalho
 *
 */
public class HttpLogReader {
	
	private final long logReaderDelay = 10000L; // 10 seconds

	public void readFile(String filePath, int trafficThreshold) throws Exception {

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
			throw e;
		} catch (IOException e) {
			// TODO if seek pos is less than 0 or if an I/O error occurs.
			e.printStackTrace();
		}

		boolean shutdown = false;

		long currentTimeMillis = System.currentTimeMillis();
		long previousTimeMillis = currentTimeMillis;

		List<HttpLog> logs = new ArrayList<>();
		
		while (!shutdown) {
			// verify if reader delay time has been passed
			if ((currentTimeMillis - previousTimeMillis) >= logReaderDelay) {
				System.out.println(logReaderDelay / 1000 + " seconds have passed.");
				previousTimeMillis = currentTimeMillis;
				
				Stats stats = new Stats();
				long getHits = logs.stream().filter(s -> s.getHttpMethod().equals("GET")).count();
				long postHits = logs.stream().filter(s -> s.getHttpMethod().equals("POST")).count();

				stats.setGet((int) getHits);
				stats.setPost((int) postHits);
				stats.setHits((int) (getHits + postHits));
				
				System.out.println(stats);
				
				logs = new ArrayList<>();
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
			} else {
				HttpLog httpLog = HttpLogParser.parse(line);
				logs.add(httpLog);
			}

			currentTimeMillis = System.currentTimeMillis();
		}

		if (shutdown) {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
