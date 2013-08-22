package com.example.rest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	        Toast.makeText(this, "Ejecutando Servicio ...", Toast.LENGTH_SHORT).show();
	    return Service.START_NOT_STICKY;    
	    }

	@Override
	    public void onCreate() {
	      super.onCreate();
	      Toast.makeText(this, "Iniciando Servicio ...", Toast.LENGTH_SHORT).show();
	}

	    @Override
	    public void onDestroy() {
	      super.onDestroy();
	      Toast.makeText(this, "Deteniendo Servicio ...", Toast.LENGTH_SHORT).show();
	}

		@Override
		public IBinder onBind(Intent arg0) {
			// TODO Auto-generated method stub
			return null;
		}   
}
