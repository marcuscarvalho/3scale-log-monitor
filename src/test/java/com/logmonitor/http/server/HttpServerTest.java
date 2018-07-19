package com.logmonitor.http.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class HttpServerTest {

	
	public static void main(String[] args) throws Exception {
		
		FileWriter fileWriter = new FileWriter("httpaccess.log");
		PrintWriter out = new PrintWriter(new BufferedWriter(fileWriter));
		
		String getProxiedRequest = "194.179.0.18, 10.16.1.2, 10.129.0.10 [13/02/2016 16:45:01] \"GET /some/path?param1=x&param2=y HTTP/1.1\" 200 0.006065388";
		String getProxiedRequest2 = "194.179.0.18, 10.15.100.200, 10.129.0.10 [13/02/2016 16:45:01] \"GET /some/path?param1=x&param2=y HTTP/1.1\" 200 0.009065388";
		String postSimpleRequest = "213.1.20.7 [13/02/2016 16:45:02] \"POST /some/other/path HTTP/1.0\" 201 0.012901348";
		
		// for testing total get stats
		for (int i = 1; i <= 10; i++) {
			out.println(getProxiedRequest);
			out.flush();
			Thread.sleep(1000L);
		}

		// for testing total post stats
		for (int i = 1; i <= 7; i++) {
			out.println(postSimpleRequest);
			out.flush();
			Thread.sleep(1000L);
		}

		// threshold above alarm
		for (int i = 1; i <= 100; i++) {
			out.println(getProxiedRequest2);
			out.flush();
			Thread.sleep(10L);
		}
		
		// threshold below alarm
		for (int i = 1; i <= 3; i++) {
			out.println(postSimpleRequest);
			out.flush();
			Thread.sleep(30000L);
		}
		
//		100.1.10.11, 89.2.1.13, 64.200.1.20, 10.1.10.16 * OK *
//		80.139.20.2, 190.9.11.1, 89.2.1.13, 64.200.1.20, 10.1.10.15 * OK *
//		193.176.1.4, 64.200.1.20, 10.1.10.15 * OK *
//		77.8.100.30, 109.1.20.3, 64.200.1.20, 68.35.10.101, 10.1.10.13 * ALERT *!
		
		String[] proxyChainRequests = {
				"100.1.10.11, 89.2.1.13, 64.200.1.20, 10.1.10.16 [13/02/2016 16:45:01] \"GET /some/path?param1=x&param2=y HTTP/1.1\" 200 0.006065388",
				"80.139.20.2, 190.9.11.1, 89.2.1.13, 64.200.1.20, 10.1.10.15 [13/02/2016 16:45:01] \"GET /some/path?param1=x&param2=y HTTP/1.1\" 200 0.006065388",
				"193.176.1.4, 64.200.1.20, 10.1.10.15 [13/02/2016 16:45:01] \"GET /some/path?param1=x&param2=y HTTP/1.1\" 200 0.006065388",
				"77.8.100.30, 109.1.20.3, 64.200.1.20, 68.35.10.101, 10.1.10.13 [13/02/2016 16:45:01] \"GET /some/path?param1=x&param2=y HTTP/1.1\" 200 0.006065388"
		};
		
		// to test inefficient proxy chain
		for (String string : proxyChainRequests) {
			out.println(string);
			out.flush();
			Thread.sleep(1000L);
		}
		
		if (fileWriter != null) {
			fileWriter.close();
		}
		
		if (out != null) {
			out.close();
		}
	}

}
