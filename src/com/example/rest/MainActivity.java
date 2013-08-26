package com.example.rest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private int check = 0;
	
	private EditText setStartHours;
	private EditText setStartMinute;
	private EditText setEndHours;
	private EditText setEndMinute;
	private EditText setPeriodicHours;
	private EditText setPeriodicMinute;
	
	private Button installTimer;
	
	private AlarmManagerBroadcastReceiver alarm;
	
	private SharedPreferences sPref;
	
	private String SAVED_TEXT = "saved check";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		installTimer = (Button) findViewById(R.id.set);
		
		setStartHours = (EditText) findViewById(R.id.startHours);
		setStartMinute = (EditText) findViewById(R.id.startMinute);
		setEndHours = (EditText) findViewById(R.id.endHours);
		setEndMinute = (EditText) findViewById(R.id.endMinute);
		setPeriodicHours = (EditText) findViewById(R.id.periodicHours);
		setPeriodicMinute = (EditText) findViewById(R.id.periodicMinute);
		
		setStartHours.setOnKeyListener (new OnKeyListener(){
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				String stringOfHours = setStartHours.getText().toString();
				if (stringOfHours.length() != 0) {	
					int hours = Integer.valueOf(stringOfHours);
					if (hours >24) {
						errorTime(arg0, setStartHours);
					} else if(setStartHours.getText().length() == 2) {
						setStartMinute.requestFocus();
					}
				}
				return false;
			}
		});
		setStartMinute.setOnKeyListener (new OnKeyListener(){
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				String stringOfMinute = setStartMinute.getText().toString();
				if (stringOfMinute.length() != 0) {
					int minute = Integer.valueOf(stringOfMinute);
					if (minute > 60) {
						errorTime(arg0, setStartMinute);
					} else if(setStartMinute.getText().length() == 2) {
						setEndHours.requestFocus();
					}
				}
				return false;
			}
		});
		setEndHours.setOnKeyListener (new OnKeyListener(){
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				String stringOfHours = setEndHours.getText().toString();
				if (stringOfHours.length() != 0) {
					int hour = Integer.valueOf(stringOfHours);
					if (hour > 24) {
						errorTime(arg0,setEndHours);
					} else if(setEndHours.getText().length() == 2) {
						setEndMinute.requestFocus();
					}
				}
				return false;
			}
		});
		setEndMinute.setOnKeyListener (new OnKeyListener(){
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				String stringOfMinute = setEndMinute.getText().toString();
				if (stringOfMinute.length() != 0) {
				int minute = Integer.valueOf(stringOfMinute);
					if (minute >60) {
						errorTime(arg0, setEndMinute);
					} else if(setEndMinute.getText().length() == 2) {
						setPeriodicHours.requestFocus();
					}
				}
				return false;
			}
		});
		setPeriodicHours.setOnKeyListener (new OnKeyListener(){
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				String stringOfHours = setPeriodicHours.getText().toString();
				if (stringOfHours.length() != 0) {
					int hour = Integer.valueOf(stringOfHours);
					if (hour > 24) {
						errorTime(arg0, setPeriodicHours);
					} else if(setPeriodicHours.getText().length() == 2) {
						setPeriodicMinute.requestFocus();
					}
				}
				return false;
			}
		});
		setPeriodicMinute.setOnKeyListener (new OnKeyListener(){
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				String stringOfMinute = setPeriodicMinute.getText().toString();
				if (stringOfMinute.length() != 0) {
					int minute = Integer.valueOf(stringOfMinute);
					if (minute > 60) {
						errorTime(arg0, setPeriodicMinute);
					} else if(setPeriodicMinute.getText().length() == 2) {
						installTimer.requestFocus();
					}
				}
				return false;
			}
		});
		installTimer.setOnClickListener(this);
		
		loadCheck();
		if (check == 1) {
			installTimer.setText("Остановить таймер");
		}
		
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
					imm.hideSoftInputFromWindow(setStartHours.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(setStartMinute.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(setEndHours.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(setEndMinute.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(setPeriodicHours.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(setPeriodicMinute.getWindowToken(), 0);
					
				if (check == 0) {
					String stringStartHours = setStartHours.getText().toString();
					String stringStartMinute = setStartMinute.getText().toString();
					String stringEndHours = setEndHours.getText().toString();
					String stringEndMinute = setEndMinute.getText().toString();
					String stringPeriodicHours = setPeriodicHours.getText().toString();
					String stringPeriodicMinute = setPeriodicMinute.getText().toString();
					if (stringStartHours.length() == 0 || stringStartMinute.length() == 0 
							|| stringEndHours.length() == 0 || stringEndMinute.length() == 0
							|| stringPeriodicHours.length() == 0 || stringPeriodicMinute.length() == 0) {
						Toast.makeText(this, "Всё поля должны быть заполнены", Toast.LENGTH_SHORT).show();
					} else {
					int startHoursInt = Integer.valueOf(stringStartHours);
					int startMinuteInt = Integer.valueOf(stringStartMinute);
					int endHoursInt = Integer.valueOf(stringEndHours);
					int endMinuteInt = Integer.valueOf(stringEndMinute);
					int periodicHoursInt = Integer.valueOf(stringPeriodicHours);
					int periodicMinuteInt = Integer.valueOf(stringPeriodicMinute);
					installTimer.setText("Остановить таймер");
					Context context = this.getApplicationContext();
				    alarm.SetAlarm(context, startHoursInt, startMinuteInt, endHoursInt, endMinuteInt, periodicHoursInt, periodicMinuteInt);
				    check =1;
				    saveCheck();
					}
				} else {
					Context context = this.getApplicationContext();
				    alarm.CancelAlarm(context);
				    installTimer.setText("Установить таймер");
				    check = 0;
				    saveCheck();
				}
				
		}
	}
	
	 void saveCheck() {
		    sPref = getPreferences(MODE_PRIVATE);
		    Editor ed = sPref.edit();
		    String stringStartHours = setStartHours.getText().toString();
			String stringStartMinute = setStartMinute.getText().toString();
			String stringEndHours = setEndHours.getText().toString();
			String stringEndMinute = setEndMinute.getText().toString();
			String stringPeriodicHours = setPeriodicHours.getText().toString();
			String stringPeriodicMinute = setPeriodicMinute.getText().toString();
		    String savedText = String.valueOf(check+","+stringStartHours+","+stringStartMinute+","
		    		+stringEndHours+","+stringEndMinute+","
		    		+stringPeriodicHours+","+stringPeriodicMinute);
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
		    	setStartHours.setText(avaliable[1]);
			    setStartMinute.setText(avaliable[2]);
			    setEndHours.setText(avaliable[3]);
			    setEndMinute.setText(avaliable[4]);
			    setPeriodicHours.setText(avaliable[5]);
			    setPeriodicMinute.setText(avaliable[6]);
		    	}
		    }
		    
		  }
		    public void errorTime(View v, EditText set) {
		    	Toast.makeText(this, "не правильное время", Toast.LENGTH_SHORT).show();
		    	set.setText("");
		    }
}
