package com.hive365.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HeadSetReceiver extends BroadcastReceiver {
	ReceiverHandler handler;
	
	public HeadSetReceiver() {
		handler = ReceiverHandler.getInstance();
	}

	@Override public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            
            switch (state) {
            case 0:
            	handler.stop();
                //Log.d(TAG, "Headset unplugged");
                break;
            case 1:
                //Log.d(TAG, "Headset plugged");
                break;
            }
        }
    }
}
