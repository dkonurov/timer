package com.example.rest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

public class TimerService extends Service {
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	        someTask();
	    return Service.START_NOT_STICKY;    
	    }

	@Override
	    public void onCreate() {
	      super.onCreate();
	}

	    @Override
	    public void onDestroy() {
	      super.onDestroy();
	}

		@Override
		public IBinder onBind(Intent arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		public void someTask() {
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			//first 0 it's now and vibrate, sleep, vibrate,sleep,..
			long[] pattern = {0, 500, 100, 500, 100, 500, 100, 500, 100, 500}; 
			v.vibrate(pattern, -1);
		}
}
