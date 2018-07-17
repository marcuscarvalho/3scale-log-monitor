package com.logmonitor.http.log.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.logmonitor.http.log.HttpLog;
import com.logmonitor.http.log.exception.LogParseException;

public class HttpLogParser {
	
	/**
	 * Parse log lines from HTTP Server access logs.
	 * E.g.
	 * 194.179.0.18, 10.16.1.2, 10.129.0.10 [13/02/2016 16:45:01] "GET /some/path?param1=x&param2=y HTTP/1.1" 200 0.006065388
	 * 
	 * @param logLine
	 * @return HttpLog Log line parsed to an object of HttpLog
	 * @throws LogParseException 
	 */
	public static HttpLog parse(String logLine) throws LogParseException {
		
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
		    
		    try {
		    	int statusCode = Integer.parseInt(responseStatusCode);
		    	httpLog.setResponseStatusCode(statusCode);
		    } catch (NumberFormatException e) {
				throw new LogParseException("Http Response Status Code is not an Integer value or is empty");
			}
		    
		    try {
		    	float responseTime = Float.parseFloat(responseTimeInSeconds);
		    	httpLog.setResponseTimeInSeconds(responseTime);
		    } catch (NumberFormatException e) {
				throw new LogParseException("Http Response Time is not an Float value or is empty");
			}
		}
		
		return httpLog;
	}

	protected static String parseOriginAddress(String addresses) throws LogParseException {
		
		String originAddress = null;
		
		final Pattern addressPattern = Pattern
				.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");

		Matcher matcher = addressPattern.matcher(addresses);
		
		if (matcher.find()) {
			originAddress = matcher.group();
		}
		
		if (originAddress == null) {
			throw new LogParseException("IP addresses in a bad format or is empty");
		}
		
		return originAddress;
	}

	protected static String[] parseProxies(String addresses) throws LogParseException {
		
		String[] proxies = null;
		
		final Pattern addressPattern = Pattern
				.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");

		Matcher matcher = addressPattern.matcher(addresses);
		
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		
		if (count == 0) {
			throw new LogParseException("IP addresses in a bad format or is empty");
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
	
	protected static LocalDateTime parseRequestDateTime(String requestDateTime) throws LogParseException {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime dateTime;
		try {
			dateTime = LocalDateTime.parse(requestDateTime, dateFormatter);

		} catch (DateTimeParseException e) {
			throw new LogParseException("Log Date time in a bad format");
		} catch (Exception e) {
			throw new LogParseException(e);
		}
		return dateTime;
	}

}
