package com.example.rest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener {
	
	private static int START_TIME = 0;
	private static int END_TIME = 2;
	private static int PERIODIC_TIME = 4;
	
	private LinearLayout mContainerView;

	private EditText[] timer = new EditText[6];
	
	private LinearLayout periodic;
	
	private boolean setSound = true, setVibration = true, setDate = true;
	
	public static Button installTimer;
	private CheckBox editView;
	
	private Button sound;
	private Button vibration;
	private Button date;
	
	private Context context;
	
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
		
		installTimer = (Button) findViewById(R.id.set);
		
		editView = (CheckBox) findViewById(R.id.checkPeriodic);
		
		sound = (Button) findViewById(R.id.sound);
		vibration = (Button) findViewById(R.id.vibration);
		date = (Button) findViewById(R.id.setDate);
		
		context = MainActivity.this;

		timer[0] = (EditText) findViewById(R.id.startHours);
		timer[1] = (EditText) findViewById(R.id.startMinute);
		timer[2] = (EditText) findViewById(R.id.endHours);
		timer[3] = (EditText) findViewById(R.id.endMinute);
		
		Drawable shape = getResources().getDrawable(R.drawable.text_for_timer);
		for (int i = 4; i <= 5; i++){
		timer[i] = new EditText(this);
		timer[i].setFilters(new InputFilter[]{
			new InputFilter.LengthFilter(2)	
		});
		timer[i].setBackgroundDrawable(shape);
		timer[i].setId(i);
		timer[i].setInputType(InputType.TYPE_CLASS_NUMBER);
		int px = (int)dpToPx(50);
		timer[i].setLayoutParams(new LayoutParams(px, LayoutParams.WRAP_CONTENT));
		timer[i].setTextColor(Color.BLACK);

		}
		for (int i=0; i<=5; i++) {
			timer[i].setFocusable(false);
		}
		
		mContainerView = (LinearLayout) findViewById(R.id.scrollContent);
		
		installTimer.setOnClickListener(this);
		editView.setOnClickListener(this);
		sound.setOnClickListener(this);
		vibration.setOnClickListener(this);
		
		for(int i=0; i<=5; i++) {
			timer[i].setOnClickListener(this);
		}
		
		loadCheck();
		
		periodic = new LinearLayout(MainActivity.this);
		TextView dots = new TextView(MainActivity.this);
		dots.setTextColor(Color.parseColor("#000000"));
		dots.setText(":");
		periodic.addView(timer[4]);
		periodic.addView(dots);
		periodic.addView(timer[5]);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		periodic.setOrientation(LinearLayout.HORIZONTAL);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		int px = (int)dpToPx(20);
		params.topMargin = px;
		periodic.setLayoutParams(params);
		
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
				break;
		}
	
		return super.onOptionsItemSelected(item);
		
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		CustomTimePickerDialog timePicker;
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
					alarm.setChecker(setSound, setVibration);
					if(checkPeriodic){
				    alarm.SetAlarm(context, intTimer[0], intTimer[1], intTimer[2], intTimer[3], intTimer[4], intTimer[5], setDate);
					} else {
						alarm.SetAlarm(context, intTimer[0], intTimer[1], intTimer[2], intTimer[3], setDate);
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
					createPeriodic(true);
					checkPeriodic = true;
				}
				else {
					anim = AnimationUtils.loadAnimation(this, R.anim.disappearance);
					anim.setAnimationListener(new Animation.AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							mContainerView.removeView(periodic);
						}
					});
					periodic.startAnimation(anim);
					timer[4].setText("");
					timer[5].setText("");
					checkPeriodic = false;
				}
				break;
			case R.id.startHours:
				showDialog(START_TIME);
				break;
			case R.id.startMinute:
				showDialog(START_TIME);
				break;
			case R.id.endHours:
				showDialog(END_TIME);
				break;
			case R.id.endMinute:
				showDialog(END_TIME);
				break;
			case 4:
				showDialog(PERIODIC_TIME);
				break;
			case 5:
				showDialog(PERIODIC_TIME);
				break;
			case R.id.sound:
				
				if (!setSound) {
					setSound = true;
				} else {
					setSound = false;
				}
				
				break;
			case R.id.vibration:
				
				if (!setVibration) {
					setVibration = true;
				} else {
					setVibration = false;
				}
				break;
			case R.id.setDate:
				
				if (!setDate) {
					setDate = true;
				} else {
					setDate = false;
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
			
			protected Dialog onCreateDialog(int id) {
				int seterHours;
				int seterMinute;
				if (id == START_TIME) {
					seterHours = setHour;
					seterMinute = setMinute;
				} else if (id == END_TIME) {
					seterHours = setHour;
					seterMinute = setMinute;
				} else {
					seterHours = periodicHour;
					seterMinute = periodicMinute;
				}
				CustomTimePickerDialog TimePicker = new CustomTimePickerDialog(context, timer[id], timer [id+1],seterHours, seterMinute);
				return TimePicker;
			}
			
			public void createPeriodic(boolean check) {
				if (check) {
					anim = AnimationUtils.loadAnimation(this,R.anim.appearance);
					periodic.startAnimation(anim);
				}
				
				mContainerView.addView(periodic);
			}
}


