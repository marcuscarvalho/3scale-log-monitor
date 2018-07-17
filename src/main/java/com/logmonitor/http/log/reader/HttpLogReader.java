package com.logmonitor.http.log.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
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
	
	public static void main(String[] args) throws Exception {
		HttpLogReader httpLogReader = new HttpLogReader();
		httpLogReader.readFile("httpaccess.log", 60);
	}

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
			throw new Exception("Log file does not exist, please check the file path passed as argument.");
		} catch (IOException e) {
			// TODO if seek pos is less than 0 or if an I/O error occurs.
			e.printStackTrace();
		}

		boolean shutdown = false;

		long currentTimeMillis = System.currentTimeMillis();
		long previousTimeMillis = currentTimeMillis;

		Stats stats = new Stats();
		Map<String, Integer> proxyHits = new HashMap<>();
		
		while (!shutdown) {
			// verify if reader delay time has been passed
			if ((currentTimeMillis - previousTimeMillis) >= logReaderDelay) {
				System.out.println(logReaderDelay / 1000 + " seconds have passed.");
				previousTimeMillis = currentTimeMillis;
				
				if (proxyHits != null && !proxyHits.isEmpty()) {
					String mostUsedProxy = proxyHits.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
					Integer mostUsedProxyHits = proxyHits.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();

					stats.setMostUsedProxy(mostUsedProxy);
					stats.setMostUsedProxyHits(mostUsedProxyHits);
				}
				
				// TODO: extract
				Gson gson = new Gson();

				// Java object to JSON, and assign to a String
				String statsJson = gson.toJson(stats);

				System.out.println(statsJson);
				
				stats = new Stats();
				proxyHits = new HashMap<>();
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
				// TODO parse throws exception, add bad_lines + 1
				HttpLog httpLog = HttpLogParser.parse(line);
				
				if (httpLog.getHttpMethod().equals("GET")) {
					stats.setGet((stats.getGet() + 1)); // TODO create an utility method
				} else if (httpLog.getHttpMethod().equals("POST")) {
					stats.setPost((stats.getPost() + 1));
				} else {
					
				}
				
				if (httpLog.getProxies() != null && httpLog.getProxies().length > 0) {
					stats.setForwardedHits((stats.getForwardedHits() + 1));
				}
				
				if (httpLog != null && httpLog.getProxies() != null) {
					for (String proxy : httpLog.getProxies()) {
						int proxyHitsCount = 1;
						if (proxyHits.get(proxy) != null) {
							proxyHitsCount += proxyHits.get(proxy);
						}
						proxyHits.put(proxy, proxyHitsCount);
					}
				}
				
				// adding 1 object per line may increase the heap memory unnecessarily 
				// removing list ojbect of logs
//				logs.add(httpLog);
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
