package com.clubscaddy.custumview;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboadSetting {
	
	public static void hideKeyboard(Context mContext){
		//Hide a keypad
		((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	} 

	public static void showKeyboard(Context mContext,EditText edittext){
		InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(edittext, InputMethodManager.SHOW_FORCED);
	}

	public static void hideKeyboard(Context mContext,EditText edittext){
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
	}

}
