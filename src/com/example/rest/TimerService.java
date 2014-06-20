package com.example.rest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.IBinder;
import android.os.Vibrator;

import com.example.rest.activity.MainActivity;

public class TimerService extends Service {
	private boolean sound;
	private boolean vibration;
	private boolean once;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		sound = intent.getBooleanExtra("sound", false);
		vibration = intent.getBooleanExtra("vibration", false);
		once = intent.getBooleanExtra("once", false);
		
		if (once) {
			Drawable shape = getResources().getDrawable(R.drawable.button_blue_selector);
			MainActivity.installTimer.setBackgroundDrawable(shape);
			MainActivity.installTimer.setText("Установить таймер");
		}
		
		vibrationAndSound();
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
		
		public void vibrationAndSound() {
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			final SoundPool player = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			final int PoolShoot = player.load(getBaseContext(), R.raw.shout, 1);
			player.setOnLoadCompleteListener(new OnLoadCompleteListener() {

				@Override
				public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
					
					if (sound) {
						player.play(PoolShoot, 1, 1, 0, 0, 1);
					}
					
				}
				
			});
			
			//first 0 it's now and next vibrate, sleep, vibrate,sleep,..
			long[] pattern = {0, 500, 100, 500, 100, 500, 100, 500, 100, 500};
			if (vibration) {
			v.vibrate(pattern, -1);
			}
		}
}
