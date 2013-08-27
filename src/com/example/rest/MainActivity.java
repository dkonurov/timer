package com.example.rest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private int check = 0;

	private EditText[] timer = new EditText[6];
	
	private Button installTimer;
	
	private AlarmManagerBroadcastReceiver alarm;
	
	private SharedPreferences sPref;
	
	private int counter=0;
	
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
		
		
		for (counter=0; counter<=5; counter++) {
			final int constN = counter;
			timer[constN].setOnKeyListener (new OnKeyListener(){
				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					if (arg2.getAction() == KeyEvent.ACTION_UP){
						if (constN!= 5) {
							if(timer[constN].getText().length() == 2) {
								timer[constN+1].requestFocus();
							}
						} else {
							if(timer[constN].getText().length() == 2) {
								installTimer.requestFocus();
							}
						}
					}
					return false;
				}
			});
		}

		installTimer.setOnClickListener(this);
		
		loadCheck();
		
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
				
		}
	}
	
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
