package com.hive365.android.fragments;

import com.hive365.android.MainActivity;
import com.hive365.android.R;
import com.hive365.android.RadioService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SongsPageFragment extends PageFragment{
	ListView lastSongs;
	ArrayAdapter<String> arrayAdapter = null;
	
	public SongsPageFragment() {
		super(R.layout.songs_page);
	}

	@Override
	public String getTitle() {
		return "Last songs";
	}

	@Override
	public void loadLayout(View v) {
		lastSongs = (ListView)v.findViewById(R.id.lastSongs);
		
		updateList();
	}
	
	@Override
	protected void bind(){
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(infoReceiver, new IntentFilter(RadioService.infoIntent));
		updateList();
	}
	
	@Override
	protected void unbind(){
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(infoReceiver);
	}
	
	private void updateList(){
		if(lastSongs!=null && getRadioService()!=null){
			if(arrayAdapter == null){
				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SongsPageFragment.this.getActivity(), android.R.layout.simple_list_item_1, getRadioService().getSongs());
				lastSongs.setAdapter(arrayAdapter);
			} else {
				arrayAdapter.notifyDataSetChanged();
			}
		}
	}

	private BroadcastReceiver infoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getStringExtra("type")=="newsong"){
				updateList();
			}
		}
	};
	
	
}
