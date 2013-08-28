package com.example.rest;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	protected String LOG_TAG = "MyLog";

	private EditText[] timer = new EditText[6];
	
	private Button installTimer;
	
	private AlarmManagerBroadcastReceiver alarm;
	
	private SharedPreferences sPref;
	
	private int count = 0;
	private static int START_TIME = 0;
	private static int END_TIME = 2;
	private static int PERIODIC_TIME = 4;
	private int check = 0;
	private int myHour = 0;
	private int myMinute = 0;
	
	private String SAVED_TEXT = "saved check";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		installTimer = (Button) findViewById(R.id.set);

		
		timer[0] = (EditText) findViewById(R.id.startHours);
		timer[1] = (EditText) findViewById(R.id.startMinute);
		timer[2] = (EditText) findViewById(R.id.endHours);
		timer[3] = (EditText) findViewById(R.id.endMinute);
		timer[4] = (EditText) findViewById(R.id.periodicHours);
		timer[5] = (EditText) findViewById(R.id.periodicMinute);

		installTimer.setOnClickListener(this);
		
		for(int i=0; i<=5; i++) {
			timer[i].setOnClickListener(this);
		}
		
		loadCheck();
		
		for (int i=0;i<=5;i++) {
			if(i<=1) {
				count = START_TIME;
			} else if (i<=3) {
				count = END_TIME;
			} else {
				count = PERIODIC_TIME;
			}
			Log.d(LOG_TAG, "i ="+i);
			timer[i].setOnTouchListener(new OnTouchListener(){
				final int id = count;
				@SuppressWarnings("deprecation")
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					showDialog(id);
					return false;
				}
			});
		}
		
		alarm = new AlarmManagerBroadcastReceiver();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_help:
			Intent Help = new Intent(MainActivity.this, Help.class);
			startActivity(Help);
		}
	
		return super.onOptionsItemSelected(item);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.set:
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					for (int i=0; i<=5;i++) {
						imm.hideSoftInputFromWindow(timer[i].getWindowToken(), 0);
					}
					
				if (check == 0) {
					String[] stringTimer = new String[6];
					int[] intTimer = new int[6];
					for (int i=0;i<=5;i++) {
						stringTimer[i] = timer[i].getText().toString();
						if (stringTimer[i].length() == 0) {
							Toast.makeText(this, "все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
						} else {
							intTimer[i] = Integer.valueOf(stringTimer[i]);
							if (i%2 == 0) {
								if (intTimer[i] > 23) {
									Toast.makeText(this, "Не правильное время", Toast.LENGTH_SHORT).show();
									timer[i].setText("");
									timer[i].requestFocus();
									return;
								}
							} else {
								if (intTimer[i] > 60) {
									Toast.makeText(this, "не правильное время", Toast.LENGTH_SHORT).show();
									timer[i].setText("");
									timer[i].requestFocus();
									return;
								}
							}
						}
					}
					installTimer.setText("Остановить таймер");
					Context context = this.getApplicationContext();
				    alarm.SetAlarm(context, intTimer[0], intTimer[1], intTimer[2], intTimer[3], intTimer[4], intTimer[5]);
				    check =1;
				    saveCheck();
				} else {
					Context context = this.getApplicationContext();
				    alarm.CancelAlarm(context);
				    installTimer.setText("Установить таймер");
				    check = 0;
				    saveCheck();
				}
			    break;
		}
	}
	
	@SuppressWarnings("deprecation")
	protected Dialog onCreateDialog(int id) {
		if (id == START_TIME || id == END_TIME || id == PERIODIC_TIME) {
			count = id;
			TimePickerDialog tpd = new TimePickerDialog(this, myCallBack, myHour, myMinute, true);
			return tpd;
	}
		return super.onCreateDialog(id);
	}
	
	 OnTimeSetListener myCallBack = new OnTimeSetListener() {
		    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		    	final int id = count;
		    	Log.d(LOG_TAG, "id ="+id);
		      myHour = hourOfDay;
		      myMinute = minute; 
		      timer[id].setText(myHour+"");
		      timer[id+1].setText(myMinute+"");
		    }
	 };
	
	 void saveCheck() {
		    sPref = getPreferences(MODE_PRIVATE);
		    Editor ed = sPref.edit();
		    String[] stringTimer = new String[6]; 
		    for (int i=0;i<=5;i++) {
		    	stringTimer[i] = timer[i].getText().toString();
		    }
		    String savedText = String.valueOf(check);
		    for (int i=0;i<=5;i++) {
		    	savedText += ",";
		    	savedText += stringTimer[i];
		    }
		    ed.putString(SAVED_TEXT, savedText);
		    ed.commit();
		  }
		  
		  void loadCheck() {
		    sPref = getPreferences(MODE_PRIVATE);
		    String savedText = sPref.getString(SAVED_TEXT, "");
		    if (savedText.length() == 0) {
		    	check = 0;
		    } else {
		    String[] avaliable = savedText.split(",");
		    check = Integer.valueOf(avaliable[0]);
		    if (check == 1) {
		    	for (int i=1; i<=6; i++) {
		    		timer[i-1].setText(avaliable[i]);
		    	}
		    	installTimer.setText("Остановить таймер");
		    }
		    }
		    
		  }
		    public void errorTime(View v, EditText set) {
		    	Toast.makeText(this, "не правильное время", Toast.LENGTH_SHORT).show();
		    	set.setText("");
		    }
}
