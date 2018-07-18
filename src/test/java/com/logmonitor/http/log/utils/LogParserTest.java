package com.logmonitor.http.log.utils;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

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

		final String regex = "([(\\d\\.\\,\\s*)]+)[\\x20-\\x2F]+\\[(.*?)\\][\\x20-\\x2F]+\"(.*?)\"[\\x20-\\x2F]+(\\d+)[\\x20-\\x2F]+(\\d+\\.\\d+)$";
		
		final String string = "194.179.0.18, 10.16.1.2, 10.129.0.10 [13/02/2016 16:45:01] \"GET /some/path?param1=x&param2=y HTTP/1.1\" 200 0.006065388";

		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(string);

		while (matcher.find()) {
		    System.out.println("Full match: " + matcher.group(0));
		    for (int i = 1; i <= matcher.groupCount(); i++) {
		        System.out.println("Group " + i + ": " + matcher.group(i));
		    }
		}
		
	}

}
