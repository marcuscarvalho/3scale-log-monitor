package com.logmonitor.domain.types;

public enum ALERT_TYPE {
	
	TRAFFIC_ABOVE_THRESHOLD("traffic_above_threshold"),
	TRAFFIC_BELOW_THRESHOLD("traffic_below_threshold");
	
	private final String type;
	
	ALERT_TYPE(final String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
