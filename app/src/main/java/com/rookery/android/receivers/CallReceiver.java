package com.rookery.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends PhoneStateListener {
	ReceiverHandler handler;
	
	public CallReceiver() {
		handler = ReceiverHandler.getInstance();
	}

	@Override
    public void onCallStateChanged(int state, String incomingNumber) {
        String stateString = "N/A";
        switch (state) {
        case TelephonyManager.CALL_STATE_IDLE:
            stateString = "Idle";
            handler.resume();
            break;
        case TelephonyManager.CALL_STATE_OFFHOOK:
            stateString = "Off Hook";
            handler.pause();
            break;
        case TelephonyManager.CALL_STATE_RINGING:
            stateString = "Ringing";
            handler.pause();
            break;
        }
    }

}
