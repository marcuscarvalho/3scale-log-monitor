package com.logmonitor.http.log.reader;

import static org.junit.Assert.*;

import java.io.RandomAccessFile;

import org.junit.Test;

public class HttpLogReaderTest {

	@Test
	public void testCheckIfLogFileExists() throws Exception {
		HttpLogReader httpLogReader = new HttpLogReader();
		RandomAccessFile randomAccessFile = httpLogReader.createRandomAccessFile("httpaccess.log");
		assertNotNull(randomAccessFile);
	}

}
