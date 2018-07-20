package com.logmonitor.http.log.utils;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.logmonitor.http.log.HttpLog;
import com.logmonitor.http.log.exception.LogParseException;

public class LogParserTest {
	
	@Test
	public void testParseOriginAddress() throws LogParseException {
		
		String addresses = "194.179.0.18, 10.16.1.2, 10.129.0.10";
		
		String originAddress = HttpLogParser.parseOriginAddress(addresses);
		
		assertEquals("194.179.0.18", originAddress);
	}

	@Test
	public void testParseTwoHttpProxies() throws LogParseException {
		
		String addresses = "194.179.0.18, 10.16.1.2, 10.129.0.10";
		
		LinkedList<String> requestProxies = HttpLogParser.parseProxies(addresses);
		
		LinkedList<String> proxies = new LinkedList<>();
		proxies.add("10.16.1.2");
		proxies.add("10.129.0.10");
		
		assertEquals(proxies, requestProxies);
	}
	
	@Test
	public void testRequestDateTime13July() throws LogParseException {
		String requestDateTime = "13/07/2018 16:45:01";
		LocalDateTime parsedDateTime = HttpLogParser.parseRequestDateTime(requestDateTime);
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(requestDateTime, df);
		
		assertEquals(dateTime, parsedDateTime);
	}
	
	@Test
	public void testAccessLogRegex() {

		final String string = "194.179.0.18, 10.16.1.2, 10.129.0.10 [13/02/2016 16:45:01] \"GET /some/path?param1=x&param2=y HTTP/1.1\" 200 0.006065388";

		Pattern pattern = HttpLogParser.getRegex();
		final Matcher matcher = pattern.matcher(string);

		while (matcher.find()) {
		    System.out.println("Full match: " + matcher.group(0));
		    for (int i = 1; i <= matcher.groupCount(); i++) {
		        System.out.println("Group " + i + ": " + matcher.group(i));
		        switch (i) {
				case 1:
					assertEquals("194.179.0.18, 10.16.1.2, 10.129.0.10", matcher.group(i));
					break;
				case 2:
					assertEquals("13/02/2016 16:45:01", matcher.group(i));
					break;
				case 3:
					assertEquals("GET /some/path?param1=x&param2=y HTTP/1.1", matcher.group(i));
					break;
				case 4:
					assertEquals("200", matcher.group(i));
					break;
				case 5:
					assertEquals("0.006065388", matcher.group(i));
					break;
				}
		    }
		}
	}
	
	@Test(expected=LogParseException.class)
	public void testParseResponseTimeNotNumber() throws LogParseException {
		HttpLogParser.parseResponseTime(new HttpLog(), "notNumber");
	}
	
	@Test(expected=LogParseException.class)
	public void testParseResponseTimeHttpLogNull() throws LogParseException {
		HttpLogParser.parseResponseTime(null, "100");
	}

	@Test(expected=LogParseException.class)
	public void testParseStatusCodeNotNumber() throws LogParseException {
		HttpLogParser.parseStatusCode(new HttpLog(), "notNumber");
	}
	
	@Test(expected=LogParseException.class)
	public void testParseStatusCodeHttpLogNull() throws LogParseException {
		HttpLogParser.parseStatusCode(null, "100");
	}

}
