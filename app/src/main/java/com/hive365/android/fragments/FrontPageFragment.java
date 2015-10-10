package com.hive365.android.fragments;

import java.util.Calendar;

import com.hive365.android.MainActivity;
import com.hive365.android.R;
import com.hive365.android.RadioService;
import com.hive365.android.dialog.InputBox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class FrontPageFragment extends PageFragment{


	protected int prevvol;

	protected ImageButton mutetog;
	protected TextView djinfo;
	protected TextView songinfo;
	protected SeekBar volumeSlider;

	private AudioManager audioManager;

	public FrontPageFragment() {
		super(com.hive365.android.R.layout.front_interface_page);
		audioManager = MainActivity.getAudioManager();
	}

	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);

		SettingsContentObserver mSettingsContentObserver = new SettingsContentObserver( new Handler() ); 

		getActivity().getContentResolver().registerContentObserver( 
				android.provider.Settings.System.CONTENT_URI, true, 
				mSettingsContentObserver );
		setInfo();

	}

	@Override
	public String getTitle() {
		return "Home";
	}



	private void shoutout(){
		int ctime = timeSec();
		int wtime = MainActivity.getRadioService().lastShoutout + (60*2);
		if(ctime>wtime){
			Looper.prepare();
			new InputBox("Send a shoutout", getActivity()){
				@Override
				public void success(String text) {
					getRadioService().shoutout(text);
				}
			}.show();
		} else {
			MainActivity.showMessage("You need to wait "+(wtime-ctime) + " seconds for your next shoutout");
		}
	}

	private void request(){
		int ctime = timeSec();
		int wtime = getRadioService().lastRequest + (60*2);
		if(ctime>wtime){
			Looper.prepare();
			new InputBox("Send a request", getActivity()){
				@Override
				public void success(String text) {
					getRadioService().request(text);
				}
			}.show();
		} else {
			MainActivity.showMessage("You need to wait "+(wtime-ctime) + " seconds for your next request");
		}
	}

	public static int timeSec(){ 
		return (int) (System.currentTimeMillis() / 1000);
	}


	private void setButtonAction(View v, int viewId, final Runnable action){
		Button button = (Button) v.findViewById(viewId);
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				new Thread(action).start();
			}

		});
	}


	@Override
	public void loadLayout(View v){
        audioManager = MainActivity.getAudioManager();

		volumeSlider = (SeekBar) v.findViewById(R.id.volumeSlider);
		mutetog = (ImageButton) v.findViewById(R.id.mutetog);

		volumeSlider.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		volumeSlider.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		volumeSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar arg0, final int arg1, boolean arg2) {
				new Thread(new Runnable(){
					@Override
					public void run(){
						audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);

					}
				}).start();

				if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) {
					mutetog.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
				} else {
					mutetog.setImageResource(android.R.drawable.ic_lock_silent_mode);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}

		});

		mutetog.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable(){
					@Override
					public void run(){
						if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) {
							prevvol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
							audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
						} else {
							audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, prevvol, 0);
						}
					}
				}).start();
				updateVolumeElements();
			}

		});

		djinfo = (TextView) v.findViewById(R.id.djinfo);
		songinfo = (TextView) v.findViewById(R.id.songinfo);

		setButtonAction(v, R.id.start, new Runnable(){
			@Override
			public void run() {
				getRadioService().startRadio();
			}
		});

		setButtonAction(v, R.id.stop, new Runnable(){
			@Override
			public void run() {
				getRadioService().stopRadio();
			}
		});

		setButtonAction(v, R.id.choon, new Runnable(){
			@Override
			public void run() {
				getRadioService().choon();
			}
		});

		setButtonAction(v, R.id.poon, new Runnable(){
			@Override
			public void run() {
				getRadioService().poon();
			}
		});

		setButtonAction(v, R.id.djftw, new Runnable(){
			@Override
			public void run() {
				getRadioService().ftw();
			}
		});

		setButtonAction(v, R.id.shoutout, new Runnable(){
			@Override
			public void run(){
				shoutout();
			}
		});

		setButtonAction(v, R.id.request, new Runnable(){
			@Override
			public void run(){
				request();
			}
		});

		setInfo();

	}


	private void updateVolumeElements(){
		if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) {
			mutetog.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
		} else {
			mutetog.setImageResource(android.R.drawable.ic_lock_silent_mode);
		}

		volumeSlider.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
	}

	public class SettingsContentObserver extends ContentObserver {

		public SettingsContentObserver(Handler handler) {
			super(handler);
		} 

		@Override
		public boolean deliverSelfNotifications() {
			return super.deliverSelfNotifications(); 
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);

			updateVolumeElements();
		}
	}

	private void setInfo(){
		RadioService service = getRadioService();
		if(service!=null){
			if(djinfo!=null){
				djinfo.setText(service.currentDj);
			}
			if(songinfo!=null){
				songinfo.setText(service.currentSong);
			}
		}

	}

	@Override
	protected void bind(){
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(infoReceiver, new IntentFilter(RadioService.infoIntent));
		setInfo();
	}

	@Override
	protected void unbind(){
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(infoReceiver);
	}

	private BroadcastReceiver infoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getStringExtra("type")=="newsong"){
				setInfo();
			}

			if(intent.getStringExtra("type")=="newdj"){
				setInfo();
			}

			if(intent.getStringExtra("type")=="service"){
				setInfo();
			}

			if(intent.getStringExtra("type")=="nointernet"){

				if(djinfo!=null){
					djinfo.setText("Can't connect");
				}
				if(songinfo!=null){
					songinfo.setText("Can't connect");
				}
			}
		}
	};
}
