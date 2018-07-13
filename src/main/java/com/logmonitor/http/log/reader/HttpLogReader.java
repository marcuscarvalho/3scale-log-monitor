package com.logmonitor.http.log.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.logmonitor.http.log.LogLine;
import com.logmonitor.http.log.utils.LogParser;

public class HttpLogReader {

	private final long logReaderDelay = 10000L; // 10 seconds

	public void readFle(String filePath, Integer trafficThreshold) throws InterruptedException, IOException {

		if (filePath == null)
			throw new IllegalArgumentException("file path cannot be null.");
		if (trafficThreshold == null)
			throw new IllegalArgumentException("traffic Threshold cannot be null.");

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

		while (!shutdown) {
			// verify if reader delay time has been passed
			if ((currentTimeMillis - previousTimeMillis) >= logReaderDelay) {
				System.out.println(logReaderDelay / 1000 + " seconds have passed.");
				previousTimeMillis = currentTimeMillis;
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
				// TODO not implemented 
				LogLine logLine = LogParser.parse(line);
			}

			currentTimeMillis = System.currentTimeMillis();
		}

		if (shutdown) {
			reader.close();
		}
	}

}
