package com.logmonitor.http.service;

import com.google.gson.Gson;
import com.logmonitor.domain.Stats;
import com.logmonitor.http.log.HttpLog;

/**
 * Responsible to prepare the data
 * and print server status.
 * 
 * @author Marcus Carvalho
 *
 */
public interface StatsService {

	void print(Gson gson);

	void populate(HttpLog httpLog);

	void calculate();

	void addBadLine();

	Stats getStats();

}
