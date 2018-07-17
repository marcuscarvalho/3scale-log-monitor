package com.logmonitor.domain.alert;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.logmonitor.domain.types.ALERT_TYPE;

public class TrafficThresholdCrossedAlert {

	private Date timestamp;
	@SerializedName("message_type")
	private final String messageType = "alert";
	@SerializedName("alert_type")
	private ALERT_TYPE alertType;
	private final String period = "minute";
	private int threshold;
	@SerializedName("current_value")
	private int currentValue;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * A string specifying the type of alert. For this section it can be either traffic_above_threshold 
	 * or traffic_below_threshold depending on whether the cross happened upwards or 
	 * downwards of the threshold, respectively.
	 * @return
	 */
	public ALERT_TYPE getAlertType() {
		return alertType;
	}

	public void setAlertType(ALERT_TYPE alertType) {
		this.alertType = alertType;
	}

	/**
	 * The traffic_threshold value given as argument to our monitor.
	 * @return
	 */
	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	/**
	 * The amount of requests performed in the relative period (in this case, the past minute).
	 * @return
	 */
	public int getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public String getMessageType() {
		return messageType;
	}

	/**
	 * The relative period to which the alert applies. In both cases its value should be the string minute for these alerts.
	 * @return
	 */
	public String getPeriod() {
		return period;
	}

}
