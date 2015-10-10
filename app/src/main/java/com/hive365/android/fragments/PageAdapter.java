package com.hive365.android.fragments;

import java.util.ArrayList;
import java.util.List;

import com.hive365.android.MainActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter{
	private static final String[] titles = {"Last songs", "Player menu", "Schedule"};
	private List<PageFragment> pages;
	
	public PageAdapter(FragmentManager fm, MainActivity activity){
		super(fm);
		
		pages = new ArrayList<PageFragment>();
		pages.add(new SongsPageFragment());
		pages.add(new FrontPageFragment());
		pages.add(new SchedulePageFragment());
	}

	@Override
	public Fragment getItem(int pos) {
		return pages.get(pos);
	}
	
	@Override
	public CharSequence getPageTitle(int pos){
		return pages.get(pos).getTitle();
	}

	@Override
	public int getCount() {
		return pages.size();
	}
}
