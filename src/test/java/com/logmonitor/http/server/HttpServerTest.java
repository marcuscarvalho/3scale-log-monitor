package com.logmonitor.http.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

/**
 * 
 * @author Marcus Carvalho
 *
 */
public class HttpServerTest {
	
	@Test
	public void populateAccessLogFile() throws IOException, InterruptedException {
		
		String getProxiedRequest = "194.179.0.18, 10.16.1.2, 10.129.0.10 [13/02/2016 16:45:01] \"GET /some/path?param1=x&param2=y HTTP/1.1\" 200 0.006065388";
		String getProxiedRequest2 = "194.179.0.18, 10.16.1.2, 10.129.1.99 [13/02/2016 16:45:01] \"GET /some/path?param1=x&param2=y HTTP/1.1\" 500 0.006065388";
		String postSimpleRequest = "213.1.20.7 [13/02/2016 16:45:02] \"POST /some/other/path HTTP/1.0\" 201 0.012901348";

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("httpaccess.log");
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		
		PrintWriter out = new PrintWriter(new BufferedWriter(fileWriter));
		Thread.sleep(10000L);

		for (int i = 1; i <= 5; i++) {
			out.println(getProxiedRequest);
			out.flush();
			Thread.sleep(1000L);
		}
		
		for (int i = 1; i <= 5; i++) {
			out.println(getProxiedRequest2);
			out.flush();
			Thread.sleep(1000L);
		}

		for (int i = 1; i <= 5; i++) {
			out.println(postSimpleRequest);
			out.flush();
			Thread.sleep(1000L);
		}

		for (int i = 1; i <= 5; i++) {
			out.println(getProxiedRequest);
			out.flush();
			Thread.sleep(1000L);
		}
		out.close();
		
	}

}
