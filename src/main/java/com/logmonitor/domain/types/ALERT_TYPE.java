package com.logmonitor.domain.types;

import com.google.gson.annotations.SerializedName;

/**
 * Alert Type
 * 
 * @author Marcus Carvalho
 *
 */
public enum ALERT_TYPE {
	
	@SerializedName("traffic_above_threshold")
	TRAFFIC_ABOVE_THRESHOLD("traffic_above_threshold"),
	@SerializedName("traffic_below_threshold")
	TRAFFIC_BELOW_THRESHOLD("traffic_below_threshold");
	
	private final String type;
	
	ALERT_TYPE(final String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
