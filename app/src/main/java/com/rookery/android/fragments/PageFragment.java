package com.rookery.android.fragments;

import com.rookery.android.MainActivity;
import com.rookery.android.RadioService;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class PageFragment extends Fragment {
	int layoutId;
	
	public PageFragment(int lid){
		layoutId = lid;
	}
	
	protected RadioService getRadioService(){
		return MainActivity.getRadioService();
	}
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		bind();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		unbind();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		bind();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unbind();
	}
	
	protected void bind(){}
	protected void unbind(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(layoutId, container, false);
        
        loadLayout(v);
        
        return v;
    }
	
	public abstract String getTitle();
	public abstract void loadLayout(View v);

}
