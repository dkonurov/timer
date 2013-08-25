package com.example.rest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private int check = 0;
	
	private EditText setStart;
	private EditText setEnd;
	private EditText setPeriodic;
	
	private Button installTimer;
	
	 private AlarmManagerBroadcastReceiver alarm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		installTimer = (Button) findViewById(R.id.set);
		
		setStart = (EditText) findViewById(R.id.start);
		setEnd = (EditText) findViewById(R.id.end);
		setPeriodic = (EditText) findViewById(R.id.periodic);
		
		installTimer.setOnClickListener(this);
		
		alarm = new AlarmManagerBroadcastReceiver();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.set:
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(setPeriodic.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(setStart.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(setEnd.getWindowToken(), 0);
				if (check == 0) {
					String stringStart = setStart.getText().toString();
					String stringEnd = setEnd.getText().toString();
					String stringPeriodic = setPeriodic.getText().toString();
					if (stringStart.length() == 0 || stringEnd.length() == 0 || stringPeriodic.length() == 0) {
						Toast.makeText(this, "Всё поля должны быть заполнены", Toast.LENGTH_SHORT).show();
					} else {
					int startInt = Integer.valueOf(stringStart);
					int endInt = Integer.valueOf(stringEnd);
					int periodicInt = Integer.valueOf(stringPeriodic);
					installTimer.setText("Остановить таймер");
					Context context = this.getApplicationContext();
				    alarm.SetAlarm(context, startInt, endInt, periodicInt);
				    check =1;
					}
				} else {
					Context context = this.getApplicationContext();
				    alarm.CancelAlarm(context);
				    installTimer.setText("Установить таймер");
				    check = 0;
				}
				
		}
	}

}
