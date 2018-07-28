package com.clubscaddy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

	/** indicates how to behave if the service is killed */
	int mStartMode;

	/** interface for clients that bind */
	IBinder mBinder;

	/** indicates whether onRebind should be used */
	boolean mAllowRebind;

	/** Called when the service is being created. */
	@Override
	public void onCreate() {
		showLog("onCreate");
	}

	/** The service is starting, due to a call to startService() */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		showLog("onStartCommand");
		return mStartMode;
	}

	/** A client is binding to the service with bindService() */
	@Override
	public IBinder onBind(Intent intent) {
		showLog("onBind");
		return mBinder;
	}

	/** Called when all clients have unbound with unbindService() */
	@Override
	public boolean onUnbind(Intent intent) {
		showLog("onUnbind");
		return mAllowRebind;
	}

	/** Called when a client is binding to the service with bindService() */
	@Override
	public void onRebind(Intent intent) {
		showLog("onRebind");
	}

	/** Called when The service is no longer used and is being destroyed */
	@Override
	public void onDestroy() {
		showLog("onDestroy");
	}
	
	void showLog(String msg){
		Log.e("My Service",msg);
	}

}
