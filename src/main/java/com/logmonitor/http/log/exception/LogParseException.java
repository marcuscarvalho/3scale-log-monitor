package com.logmonitor.http.log.exception;

public class LogParseException extends Exception {

	private static final long serialVersionUID = 1L;

	public LogParseException() {
		super();
	}

	public LogParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LogParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogParseException(String message) {
		super(message);
	}

	public LogParseException(Throwable cause) {
		super(cause);
	}

}
