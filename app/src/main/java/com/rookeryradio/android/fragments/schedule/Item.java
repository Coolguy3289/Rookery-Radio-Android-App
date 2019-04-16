package com.rookeryradio.android.fragments.schedule;

import java.util.ArrayList;
import java.util.List;

public class Item {
	private String day;
	private List<String> shows;
	
	public Item(){
		super();
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public List<String> getShows() {
		return shows;
	}

	public void setShows(List<String> shows) {
		this.shows = shows;
	}
	
	
	
}
