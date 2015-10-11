package com.rookery.android.fragments.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rookery.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScheduleAdapter extends BaseExpandableListAdapter {
	private Context ctx;
	private HashMap<String, List<String>> shows;
	private List<String> days;
	
	public ScheduleAdapter(Context c, HashMap<String, List<String>> shows, List<String> days){
		this.shows = shows;
		this.days = days;
		ctx = c;
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		return shows.get(days.get(arg0)).get(arg1);
	}
	
	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}
	
	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		String day = (String) getChild(arg0, arg1);
		if(arg3==null){
			arg3 = LayoutInflater.from(arg4.getContext()).inflate(R.layout.item_child, arg4, false);
		}
		TextView dayView = (TextView) arg3.findViewById(R.id.child_txt);
		dayView.setText(day);
		
		return arg3;
	}
	
	@Override
	public int getChildrenCount(int arg0) {
		return shows.get(days.get(arg0)).size();
	}
	
	@Override
	public Object getGroup(int arg0) {
		return days.get(arg0);
	}
	
	@Override
	public int getGroupCount() {
		return days.size();
	}
	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}
	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		String day = (String) getGroup(arg0);
		if(arg2==null){
			arg2 = LayoutInflater.from(arg3.getContext()).inflate(R.layout.item_parent, arg3, false);
		}
		TextView dayView = (TextView) arg2.findViewById(R.id.parent_txt);
		dayView.setText(day);
		
		return arg2;
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
