package com.example.rest.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rest.AlarmManagerBroadcastReceiver;
import com.example.rest.R;
import com.example.rest.Utils;
import com.example.rest.adapters.CustomPagerAdapter;
import com.example.rest.elements.AlarmLayout;
import com.example.rest.elements.TimerLayout;


@SuppressLint("NewApi")
public class MainActivity extends Activity {

    private ViewPager pager;

    public static Utils utils;


	private static int START_TIME = 0;
	private static int END_TIME = 2;
	private static int PERIODIC_TIME = 4;

	private LinearLayout mContainerView;

	private EditText[] timer = new EditText[6];

	private LinearLayout periodic;
	
	private boolean setSound = true, setVibration = true, setDate = true;
	
	public static Button installTimer;

	private AlarmManagerBroadcastReceiver alarm;
	
	private SharedPreferences sPref;
	
	private Animation anim;

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

        utils = new Utils(this);

        pager = (ViewPager) findViewById(R.id.pager);
        TimerLayout timerLayout = new TimerLayout(this);
        AlarmLayout alarmLayout = new AlarmLayout(this, true);
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(alarmLayout, timerLayout);
        pager.setAdapter(customPagerAdapter);

		loadCheck();

		alarm = new AlarmManagerBroadcastReceiver();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	 void saveCheck() {
		    sPref = getPreferences(MODE_PRIVATE);
		    Editor ed = sPref.edit();
		    String[] stringTimer = new String[6]; 
		    for (int i=0;i<=5;i++) {
		    	stringTimer[i] = timer[i].getText().toString();
		    }
		    String savedText = String.valueOf(check);
		    savedText += ",";
		    if (setSound) {
		    	savedText += "1";
		    } else {
		    	savedText += "0";
		    }
		    
		    savedText += ",";
		    if (setVibration) {
		    	savedText += "1";
		    } else {
		    	savedText += "0";
		    }
		    savedText += ",";
		    savedText += checkPeriodic;
		    int length;
		    if (checkPeriodic) {
		    	length = 5;
		    } else {
		    	length = 3;
		    }
		    for (int i=0;i<=length;i++) {
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
		    if (Integer.parseInt(avaliable[1]) == 1) {
		    	setSound = true;
		    } else {
		    	setSound = false;
		    }
		    
		    if (Integer.parseInt(avaliable[2]) == 1) {
		    	setVibration = true;
		    } else {
		    	setVibration = false;
		    }
		    if (check == 1) {
			    checkPeriodic = Boolean.valueOf(avaliable[3]);
			    if (checkPeriodic) {
			    	createPeriodic(false);
			    }
			    int length;
			    if (checkPeriodic) {
			    	length = 10;
			    } else {
			    	length = 8;
			    }
		    	for (int i=4; i<=length; i++) {
		    		timer[i-4].setText(avaliable[i]);
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
		    
		    public float dpToPx(int dp) {
				Resources r = getResources();
				float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
		        return px;
		    }
			

			
			public void createPeriodic(boolean check) {
				if (check) {
					anim = AnimationUtils.loadAnimation(this,R.anim.appearance);
					periodic.startAnimation(anim);
				}
				
				mContainerView.addView(periodic);
			}
}


