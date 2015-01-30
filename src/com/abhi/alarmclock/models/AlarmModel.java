package com.abhi.alarmclock.models;

public class AlarmModel {

	public long id;
	public String name;
	public int time;
	public int hours;
	public int minutes;
	public long days;
	public boolean isEnabled;

	public AlarmModel() {
		//days = new boolean[7];
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public long getDays() {
		return days;
	}

	public void setDays(long days) {
		this.days = days;
	}
	public int getHours() {
		return hours;
	}


	public void setHours(int hours) {
		this.hours = hours;
	}


	public int getMinutes() {
		return minutes;
	}


	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	
}
