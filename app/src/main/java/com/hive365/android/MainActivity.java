package com.hive365.android;

import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hive365.android.dialog.CustomTimePickerDialog;
import com.hive365.android.dialog.InputBox;
import com.hive365.android.fragments.PageAdapter;
import com.widget.PagerClickTitleStrip;

public class MainActivity extends FragmentActivity{
	private PagerAdapter pageAdapter;
	private ViewPager viewPager;
	private static AudioManager audioManager;
	
	private static RadioService radioService;
	private boolean serviceBound = false;
	private static Handler handler;
	public static Context ctx;
	
	public void changeName() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				Looper.prepare();
				
				new InputBox("Set your username", MainActivity.this, radioService.getUsername()){
					@Override
					public void success(String text) {
						Editor edit = radioService.editPrefs();
						edit.putString("username", text).commit();
						//edit.commit();
						showMessage("Username set");
					}

					@Override
					public void fail(){
						showMessage("Nothing entered");
						changeName();
					}

				}.show();
			}
		}).start();
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			radioService = ((RadioService.LocalBinder)service).getService();
			
			if (radioService.getUsername().equals("Unknown")) {
				changeName();
			} else {
				showMessage("Welcome " + radioService.getUsername());
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			radioService = null;
		}
	};

	void doBindService() {
		Intent bindIntent = new Intent(this, RadioService.class);
		if(startService(bindIntent)==null){
			
		}
		bindService(bindIntent, mConnection, 0);
		
		serviceBound = true;
	}


	void doUnbindService() {
		if (serviceBound) {
			// Detach our existing connection.
			unbindService(mConnection);
			serviceBound = false;
		}
	}
	
	public static AudioManager getAudioManager(){
		return audioManager;
	}
	
	@Override
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		
		ctx = getApplicationContext();
		
		handler = new Handler();
		setContentView(R.layout.main_layout);
		doBindService();
		
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		
		PagerClickTitleStrip strip = (PagerClickTitleStrip) findViewById(R.id.pager_title_strip);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		pageAdapter = new PageAdapter(this.getSupportFragmentManager(), this);
		viewPager.setAdapter(pageAdapter);
		viewPager.setCurrentItem(1);
		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		doUnbindService();
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		doBindService();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		doUnbindService();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.settings:
	        	Intent i = new Intent(this, SettingsActivity.class);
	            startActivityForResult(i, 1);
	        	return true;
	        case R.id.play_until:
	        	playUntil();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void playUntil(){
		handler.post(new Runnable(){
			public void run(){
				CustomTimePickerDialog.OnActionListener listener = new CustomTimePickerDialog.OnActionListener(){
					@Override
					public void onAction(TimePicker view, int hourOfDay, int minute) {
						if(getRadioService()!=null){
							getRadioService().playUntil(hourOfDay, minute);
						}
					}
				};
				
				CustomTimePickerDialog dialog = new CustomTimePickerDialog(MainActivity.this, 0, 15, true, listener);
				
				dialog.setTitle("Play for how long?");
				dialog.show();
			}
		});
		
	}

	public static void showMessage(final String message){
		handler.post(new Runnable(){
			@Override
			public void run() {
				Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static RadioService getRadioService() {
		return radioService;
	}
	
	
}
