package com.clubscaddy.utility;

import com.clubscaddy.Interface.ModelManager;

import android.content.Context;

public class GlobalValues {
	private static ModelManager modelManagerObj;

	public static ModelManager getModelManagerObj(Context mContext) {
		if(modelManagerObj!=null)
		return modelManagerObj;
		else
			return new ModelManager(mContext);
	}
}
