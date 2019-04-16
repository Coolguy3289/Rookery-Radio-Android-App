package com.rookeryradio.android.dialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TimePicker;
import android.widget.Toast;

public class CustomTimePickerDialog extends TimePickerDialog {
	private OnActionListener actionListener;
	TimePicker picker;
	int hour=0, minutes=0;
	
	public CustomTimePickerDialog(Context context, int hourOfDay, int minute, boolean is24HourView, OnActionListener list) {
		super(context,null, hourOfDay, minute, is24HourView);
		actionListener = list;
		this.setButton(DialogInterface.BUTTON_POSITIVE, "Play", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    	actionListener.onAction(picker, hour, minutes);
		    }
		});
		
		this.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    	//callBack.onCancel(dialog, which);
		        dialog.cancel();
		    }
		});
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
    }
	
	@Override
	public void onTimeChanged (TimePicker view, int hourOfDay, int minute) {
		picker = view;
		hour = hourOfDay;
		this.minutes = minute;
	}
	
	public static abstract interface OnActionListener {
		public abstract void onAction(TimePicker view, int hourOfDay, int minute);
	}

}
