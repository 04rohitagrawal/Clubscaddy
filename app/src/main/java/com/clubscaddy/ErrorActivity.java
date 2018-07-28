package com.clubscaddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.clubscaddy.R;
import com.clubscaddy.utility.SessionManager;

public class ErrorActivity extends Activity{

	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_event);
		mContext = this;
		String msg = getIntent().getStringExtra("msg");
		//alertDialog(msg);
	}

	@SuppressWarnings({ "unused", "deprecation" })
	private void alertDialog(String message){
		final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		alertDialog.setMessage(message);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
				SessionManager.clearSharePref(mContext);
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
		});
		alertDialog.show();
	}

}
