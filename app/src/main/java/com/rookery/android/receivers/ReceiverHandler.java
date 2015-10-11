package com.rookery.android.receivers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.rookery.android.RadioService;

public class ReceiverHandler {
	RadioService service;
	private HeadSetReceiver hsReceiver;
	private CallReceiver cReceiver;
	private boolean paused = false;
	private static ReceiverHandler instance;
	
	public ReceiverHandler(RadioService rs){
		service = rs;
		instance = this;
		setup();
	}
	
	public static ReceiverHandler getInstance(){
		return instance;
	}
	
	private void setup(){
		hsReceiver = new HeadSetReceiver();
		cReceiver = new CallReceiver();
		
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) service.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(cReceiver, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	public void register(){
		IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
		service.registerReceiver(hsReceiver, filter);
	}
	
	public void stop(){
		if(service.getStatus())
		service.stopRadio();
	}
	
	public void pause(){
        if(service.getStatus()) {
            paused = true;
            stop();
        }
	}
	
	public void resume(){
		if(paused){
			new Thread(new Runnable(){
				public void run(){
					service.startRadio();
				}
			}).start();
			paused = false;
		}
	}
	
}
