package com.logmonitor.http.log.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.logmonitor.http.log.HttpLog;

public class HttpLogParser {
	
	/**
	 * Parse log lines from HTTP Server access logs.
	 * E.g.
	 * 194.179.0.18, 10.16.1.2, 10.129.0.10 [13/02/2016 16:45:01] "GET /some/path?param1=x&param2=y HTTP/1.1" 200 0.006065388
	 * 
	 * @param logLine
	 * @return HttpLog Log line parsed to an object of HttpLog
	 */
	public static HttpLog parse(String logLine) {
		
		HttpLog httpLog = new HttpLog();
		
		final String regex = "([(\\d\\.\\,\\s*)]+)[\\x20-\\x2F]+\\[(.*?)\\][\\x20-\\x2F]+\"(.*?)\"[\\x20-\\x2F]+(\\d+)[\\x20-\\x2F]+(\\d+\\.\\d+)$";

		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(logLine);

		if (matcher.find()) {
		    String addresses = matcher.group(1);
		    String requestDateTime = matcher.group(2);
		    String requestInfo = matcher.group(3);
		    String responseStatusCode = matcher.group(4);
		    String responseTimeInSeconds = matcher.group(5);
		    
		    String originAddress = parseOriginAddress(addresses);
		    String[] proxies = parseProxies(addresses);
		    LocalDateTime dateTime = parseRequestDateTime(requestDateTime);
		    
		    String[] requestInfoArray = requestInfo.split(" ");
		    String method = requestInfoArray[0];
		    String url = requestInfoArray[1];
		    String protocol = requestInfoArray[2];
		    		
		    httpLog.setOriginAddress(originAddress);
		    httpLog.setProxies(proxies);
		    httpLog.setRequestDateTime(dateTime);
		    httpLog.setHttpMethod(method);
		    httpLog.setUrl(url);
		    httpLog.setProtocol(protocol);
		    httpLog.setResponseStatusCode(Integer.parseInt(responseStatusCode)); // TODO need to check null value
		    httpLog.setResponseTimeInSeconds(Float.parseFloat(responseTimeInSeconds)); // TODO need to check null value
		}
		
		return httpLog;
	}

	protected static String parseOriginAddress(String addresses) {
		
		String originAddress = null;
		
		final Pattern addressPattern = Pattern
				.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");

		Matcher matcher = addressPattern.matcher(addresses);
		
		if (matcher.find()) {
			originAddress = matcher.group();
		}
		
		return originAddress;
	}

	protected static String[] parseProxies(String addresses) {
		
		String[] proxies = null;
		
		final Pattern addressPattern = Pattern
				.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");

		Matcher matcher = addressPattern.matcher(addresses);
		
		int count = 0;
		while (matcher.find()) {
			count++;
		}
			
		if (count > 1) {
			proxies = new String[count - 1];
			matcher.find(1);
			for (int i = 1; i < count; i++) {
				String proxy = matcher.group();
				proxies[i - 1] = proxy;
				matcher.find();
			}
		}
		return proxies;
	}
	
	protected static LocalDateTime parseRequestDateTime(String requestDateTime) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(requestDateTime, dateFormatter);
		return dateTime;
	}

}
