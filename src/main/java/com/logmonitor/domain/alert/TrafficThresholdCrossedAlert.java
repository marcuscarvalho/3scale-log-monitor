package com.logmonitor.domain.alert;

import java.util.Date;

import com.logmonitor.domain.types.ALERT_TYPE;

public class TrafficThresholdCrossedAlert {

	private Date timestamp;
	private final String messageType = "alert";
	private ALERT_TYPE alertType;
	private final String period = "minute";
	private int threshold;
	private int currentValue;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public ALERT_TYPE getAlertType() {
		return alertType;
	}

	public void setAlertType(ALERT_TYPE alertType) {
		this.alertType = alertType;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getPeriod() {
		return period;
	}

}
