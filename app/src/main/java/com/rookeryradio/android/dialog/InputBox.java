package com.rookeryradio.android.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

public abstract class InputBox {
	String message;
	AlertDialog.Builder builder;
	Activity ctx;

	public abstract void success(String text);

	public void fail(){
		Toast toast = Toast.makeText(ctx, "You didn't enter anything", Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public InputBox(String mes, Activity ac){
		this(mes, ac, "");
	}

	public InputBox(String mes, Activity ac, String def){
		message = mes;
		ctx = ac;//getApplicationContext();

		builder = new AlertDialog.Builder(ctx);
		builder.setTitle(message);

		final EditText input = new EditText(ctx);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		input.setText(def);
		builder.setView(input);

		builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(input.getText().length()>1){
					success(input.getText().toString());
					//showMessage(input.getText().toString());
				} else {
					fail();
				}

			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});


	}

	public void show(){
		ctx.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				builder.show();
			}
		});
		
	}
}