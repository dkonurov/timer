package com.example.rest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



public class MainActivity extends Activity implements OnClickListener {

	protected String LOG_TAG = "MyLog";

	private EditText[] timer = new EditText[6];
	
	private LinearLayout periodic;
	
	private Button installTimer;
	private CheckBox editView ;
	
	private AlarmManagerBroadcastReceiver alarm;
	
	private SharedPreferences sPref;
	
	private LinearLayout scrollConteiner;
	
	private int count = 0;
	private static int START_TIME = 0;
	private static int END_TIME = 2;
	private static int PERIODIC_TIME = 4;
	private boolean checkPeriodic = false;
	private int check = 0;
	private int setHour = 0;
	private int setMinute = 0;
	private int periodicHour = 0;
	private int periodicMinute = 0;
	
	private String SAVED_TEXT = "saved check";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		installTimer = (Button) findViewById(R.id.set);
		
		editView = (CheckBox) findViewById(R.id.checkPeriodic);

		timer[0] = (EditText) findViewById(R.id.startHours);
		timer[1] = (EditText) findViewById(R.id.startMinute);
		timer[2] = (EditText) findViewById(R.id.endHours);
		timer[3] = (EditText) findViewById(R.id.endMinute);
		for (int i = 4; i <= 5; i++){
		timer[i] = new EditText(getApplicationContext());
		timer[i].setId(i);
		timer[i].setInputType(InputType.TYPE_CLASS_NUMBER);
		int px = dpToPx(50);
		timer[i].setLayoutParams(new LayoutParams(px, LayoutParams.WRAP_CONTENT));
		timer[i].setTextColor(Color.BLACK);
		int maxLength = 10;
		InputFilter[] filter = new InputFilter[1];
		filter[0] = new InputFilter.LengthFilter(maxLength);
		timer[i].setFilters(filter);
		}
		for (int i=0; i<=5; i++) {
			timer[i].setFocusable(false);
		}
		
		scrollConteiner = (LinearLayout) findViewById(R.id.scrollContent);
		
		installTimer.setOnClickListener(this);
		editView.setOnClickListener(this);
		
		for(int i=0; i<=5; i++) {
			timer[i].setOnClickListener(this);
		}
		
		loadCheck();
		
		for (int i=0;i<=5;i++) {
			Log.d(LOG_TAG, "i ="+i);
			timer[i].setOnTouchListener(new OnTouchListener(){
				@SuppressWarnings("deprecation")
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					
					switch(arg0.getId()){
						case R.id.startHours:
							showDialog(START_TIME);
							count=START_TIME;
							break;
						case R.id.startMinute:
							showDialog(START_TIME);
							count = START_TIME;
							break;
						case R.id.endHours:
							showDialog(END_TIME);
							count = END_TIME;
							break;
						case R.id.endMinute:
							showDialog(END_TIME);
							count = END_TIME;
							break;
						case 4:
							showDialog(PERIODIC_TIME);
							count = PERIODIC_TIME;
							break;
						case 5:
							showDialog(PERIODIC_TIME);
							count = PERIODIC_TIME;
							break;
					}
					
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.set:
				if (check == 0) {
					String[] stringTimer = new String[6];
					int[] intTimer = new int[6];
					boolean chekerTime = true;
					int n;
					if (checkPeriodic) {
						n = 5;
					} else {
						n = 3;
					}
					for (int i=0;i<=n;i++) {
						stringTimer[i] = timer[i].getText().toString();
						if (stringTimer[i].length() == 0) {
							Toast.makeText(this, "все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
							chekerTime = false;
							break;
						} else {
							intTimer[i] = Integer.valueOf(stringTimer[i]);
							if (i == 5 && intTimer[4] == 0 && intTimer[5] == 0) {
								Toast.makeText(this, "должны быть время между отдыхом", Toast.LENGTH_LONG).show();
								showDialog(PERIODIC_TIME);
								chekerTime = false;
								break;
							}
						}
					}
					if (!chekerTime) {
						return;
					}
					installTimer.setText("Остановить таймер");
					installTimer.setBackgroundResource(R.drawable.button_red_selector);
					Context context = this.getApplicationContext();
					if(checkPeriodic){
				    alarm.SetAlarm(context, intTimer[0], intTimer[1], intTimer[2], intTimer[3], intTimer[4], intTimer[5]);
					} else {
						alarm.SetAlarm(context, intTimer[0], intTimer[1], intTimer[2], intTimer[3]);
					}
				    check =1;
				    saveCheck();
				} else {
					Context context = this.getApplicationContext();
				    alarm.CancelAlarm(context);
				    installTimer.setText("Установить таймер");
				    installTimer.setBackgroundResource(R.drawable.button_blue_selector);
				    check = 0;
				    saveCheck();
				}
			    break;
			case R.id.checkPeriodic:
				if(!checkPeriodic) {
					periodic = new LinearLayout(getApplicationContext());
					TextView dots = new TextView(getApplicationContext());
					dots.setText(":");
					periodic.addView(timer[4]);
					periodic.addView(dots);
					periodic.addView(timer[5]);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					periodic.setOrientation(LinearLayout.HORIZONTAL);
					params.gravity = Gravity.CENTER_HORIZONTAL;
					int px = dpToPx(20);
					params.topMargin = px;
					periodic.setLayoutParams(params);
					scrollConteiner.addView(periodic);
					checkPeriodic = true;
				}
				else {
					periodic.removeAllViews();
					scrollConteiner.removeView(periodic);
					checkPeriodic = false;
				}
				break;
		}
	}
	
	@SuppressWarnings("deprecation")
	protected Dialog onCreateDialog(int id) {
		if (id == START_TIME || id == END_TIME) {
			TimePickerDialog tpd = new TimePickerDialog(this, myCallBack, setHour, setMinute, true);
			return tpd;
		} else if (id == PERIODIC_TIME) {
			TimePickerDialog tpd = new TimePickerDialog(this, myCallBack, periodicHour, periodicMinute, true);
			return tpd;
		}
		return super.onCreateDialog(id);
	}
	
	 OnTimeSetListener myCallBack = new OnTimeSetListener() {
		    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		      timer[count].setText(hourOfDay+"");
		      timer[count+1].setText(minute+"");
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
		    savedText += ",";
		    savedText += checkPeriodic;
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
			    checkPeriodic = Boolean.valueOf(avaliable[7]);
		    	for (int i=1; i<=6; i++) {
		    		timer[i-1].setText(avaliable[i]);
		    	}
		    	installTimer.setText("Остановить таймер");
		    	installTimer.setBackgroundResource(R.drawable.button_red_selector);
		    }
		    }
		    
		  }
		    public void errorTime(View v, EditText set) {
		    	Toast.makeText(this, "не правильное время", Toast.LENGTH_SHORT).show();
		    	set.setText("");
		    }
		    
		    public int dpToPx(int dp) {
		        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
		        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
		        return px;
		    }
}
