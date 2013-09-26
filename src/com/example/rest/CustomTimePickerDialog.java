package com.example.rest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CustomTimePickerDialog implements OnClickListener, android.view.View.OnClickListener{
	
	public Dialog dialog;
	
	private LinearLayout layout;
	
	private Scroller mScroller;
	
	private int mLastScroll;
	
	private Button setTime;
	private Button hourPlus;
	private Button hourMinus;
	private Button minutePlus;
	private Button minuteMinus;
	
	private boolean checker = false;
	
	private int setText;
	
	private final GestureDetector gesture;
	
	private TextView hourSmall[] = new TextView[22];
	
	private TextView hourBig;
	
	private LinearLayout hourConteiner;
	
	private LinearLayout minuteConteiner;
	
	private OnTouchListener mGestureListener;
	
	private EditText pickerHour;
	
	private EditText timerHour;
	private EditText timerMinute;
	
	private int hour = 0;
	
	private int minute = 0;
	
	private EditText pickerMinute;

	private int LastDiff;

	private int Scroll;
	
	public CustomTimePickerDialog(Context context, EditText view1, EditText view2, int setHours, int setMinute){
		
			timerHour = view1;
			timerMinute = view2;
			dialog = new Dialog(context);
			dialog.setCanceledOnTouchOutside(true);
		    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    
		    dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		    dialog.setContentView(R.layout.fragment_time_picker);
		    
		    final View v = new View(dialog.getContext());
		    
		    mScroller = new Scroller(dialog.getContext());
		    
		    setTime = (Button) dialog.findViewById(R.id.SetTime);
		    hourPlus = (Button) dialog.findViewById(R.id.hours_plus);
		    hourMinus = (Button) dialog.findViewById(R.id.hours_minus);
		    minutePlus = (Button) dialog.findViewById(R.id.minute_plus);
		    minuteMinus = (Button) dialog.findViewById(R.id.minute_minus);
		    
		    for (int i=0; i<22; i++)
    		hourSmall[i] = new TextView(dialog.getContext());
    		hourBig = new TextView(dialog.getContext());
		    
		    hourConteiner = (LinearLayout) dialog.findViewById(R.id.hours_scroll);
		    
		    hourConteiner.setOverScrollMode(hourConteiner.OVER_SCROLL_ALWAYS);

		    minuteConteiner = (LinearLayout) dialog.findViewById(R.id.minute_container);
		    
		    ScrollView hourScroll = (ScrollView) dialog.findViewById(R.id.hours_conteiner);
		    hourScroll.setVerticalScrollBarEnabled(false);
		    
		    onTimePickerListener hourListener = new onTimePickerListener(23) {

				@Override
				public String getText() {
					// TODO Auto-generated method stub
					return pickerHour.getText().toString();
				}

				@Override
				public void setText(int set) {
					// TODO Auto-generated method stub
					pickerHour.setText(set+"");
				}

				@Override
				public Object getDialogInput() {
					// TODO Auto-generated method stub
					return dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				}

				@Override
				public IBinder getViewIBinder() {
					// TODO Auto-generated method stub
					return v.getApplicationWindowToken();
				}

				@Override
				public void focusable(boolean seter) {
					// TODO Auto-generated method stub
					pickerHour.setFocusable(seter);
				}

				@Override
				void focusableTouch(boolean seter) {
					// TODO Auto-generated method stub
					pickerHour.setFocusableInTouchMode(seter);
					pickerHour.setFocusable(seter);
				}
		    	
		    };
		    
		    onTimePickerListener minuteListener = new onTimePickerListener(59) {

				@Override
				public String getText() {
					// TODO Auto-generated method stub
					return pickerMinute.getText().toString();
				}

				@Override
				public void setText(int set) {
					// TODO Auto-generated method stub
					pickerMinute.setText(set+"");
				}

				@Override
				public Object getDialogInput() {
					// TODO Auto-generated method stub
					return dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				}

				@Override
				public IBinder getViewIBinder() {
					// TODO Auto-generated method stub
					return v.getApplicationWindowToken();
				}

				@Override
				public void focusable(boolean seter) {
					// TODO Auto-generated method stub
					pickerMinute.setFocusable(seter);
				}

				@Override
				void focusableTouch(boolean seter) {
					// TODO Auto-generated method stub
					pickerMinute.setFocusableInTouchMode(seter);
					pickerMinute.setFocusable(seter);
				}
		    	
		    };
		    
		    	mGestureListener = new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if(gesture.onTouchEvent(arg1)) {
						return true;
					}
					if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Log.v("ACTION", "ACTION UP!!!");
						//if stay pressed button this function fix bug
						isPressedButton();
						if (checker) {
							Log.v("hourConteiner", hourConteiner.getScrollY()+"");
							Log.v("getHeight", hourBig.getHeight()+"");
							double set = (double) hourConteiner.getScrollY()/hourBig.getHeight();
							int setTuda;
							if (set%1>=0.5) {
								setTuda = (int) set;
							} else {
								setTuda = (int) set-1;
							}
							//set Text after Scrolling
							setText(setTuda, pickerHour);
				    		Log.v("Action up", set+"");
				    		setText = 0;
							hourConteiner.scrollTo(0, 0);
							actionUp();
							return true;
						}
					}
		    		Log.v("xz", "");
					return false;
				}
			};
		    
		    hourPlus.setOnTouchListener(mGestureListener);
		    
		    hourMinus.setOnTouchListener(mGestureListener);
		    
		    minutePlus.setOnTouchListener(mGestureListener);
		    
		    minuteMinus.setOnTouchListener(mGestureListener);
		    
		    setTime.setOnClickListener(this);
		    hourPlus.setOnClickListener(this);
		    hourMinus.setOnClickListener(this);
		    minutePlus.setOnClickListener(this);
		    minuteMinus.setOnClickListener(this);
		    
		    pickerHour = (EditText) dialog.findViewById(R.id.hours_dislpay);
		    pickerMinute = (EditText) dialog.findViewById(R.id.minute_display);
		    
		    pickerHour.setOnClickListener(this);
		    pickerMinute.setOnClickListener(this);
		    
		    pickerHour.setFocusable(false);
		    pickerMinute.setFocusable(false);
		    
		    pickerHour.setText(hour+"");
		    pickerMinute.setText(minute+"");
		    
		    pickerHour.setId(1);
		    pickerMinute.setId(2);
		    
		    pickerHour.setOnKeyListener(hourListener);
		    
		    pickerMinute.setOnKeyListener(minuteListener);
		    
		    pickerHour.setOnFocusChangeListener(hourListener);
		    
		    pickerMinute.setOnFocusChangeListener(minuteListener);
		    
		    pickerHour.setOnLongClickListener(hourListener);
		    
		    pickerMinute.setOnLongClickListener(minuteListener);
		    
		    gesture = new GestureDetector(dialog.getOwnerActivity(), new GestureDetector.SimpleOnGestureListener(){
		    	
		    	

				@Override
		    	public boolean onDown(MotionEvent e) {
		    		mLastScroll = mScroller.getCurrY();
		    		Log.v("mLastScroll", mLastScroll+"");
		    		return false;
		    	}
		    	
		    	@Override
		    	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
		    		
		    		hourConteiner.scrollTo(0, 0);
		    		int set =hourConteiner.getScrollY()/hourBig.getHeight();
		    		setText(set, pickerHour);
		    		Log.v("onFling", set+"");
		    		setText = 0;
		    		actionUp();
		    		return true;
		    	}
		    	
		    	@Override
		    	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		    		mScroller.startScroll(0, 0, 0, (int)-distanceY);
		    		if(!checker) {
		    			Drawable shape = dialog.getContext().getResources().getDrawable(R.drawable.text_for_timer);
		    			int forHourBig;
		    			if (hour == 23) {
		    				forHourBig = 0;
		    			} else {
		    				forHourBig = hour+1;
		    			}
		    			hourBig.setText(forHourBig+"");
		    			hourBig.setTextSize(25);
		    			hourBig.setHeight((int)dpToPx(50));
		    			hourBig.setWidth((int)dpToPx(50));
		    			hourBig.setTextColor(Color.BLACK);
		    			hourBig.setGravity(Gravity.CENTER);
		    			hourBig.setBackgroundDrawable(shape);
		    			hourConteiner.addView(hourBig, 1);
		    			hourPlus.setVisibility(View.INVISIBLE);
		    			hourBig.setVisibility(View.VISIBLE);
		    			int forHourSmall = 0;
		    			if (hour == 0) {
		    	             forHourSmall = 23;
		    	           } else {
		    	        	   forHourSmall = hour-1;
		    	           }
		    			for (int i=0; i<22; i++) {
		    				hourSmall[i].setText(forHourSmall+"");
		    				hourSmall[i].setOnTouchListener(mGestureListener);
		    				hourSmall[i].setTextSize(25);
		    				hourSmall[i].setGravity(Gravity.CENTER);
		    				hourSmall[i].setHeight((int)dpToPx(50));
		    				hourSmall[i].setWidth((int)dpToPx(50));
		    				hourSmall[i].setTextColor(Color.BLACK);
		    				hourSmall[i].setBackgroundDrawable(shape);
		    				hourConteiner.addView(hourSmall[i],i+3);
		    			    if (forHourSmall == 0){
		    			    	forHourSmall = 23;
		    			    } else {
		    			    	forHourSmall--;
		    			    }
		    			    hourMinus.setVisibility(View.INVISIBLE);
		    			    hourSmall[i].setVisibility(View.VISIBLE);
		    			}
		    			Log.v("hour", hour+"");
		    			Log.v("forHourBig",forHourBig+"");
		    			Log.v("forHourSmall", forHourSmall+"");
		    			checker = true;
		    		}  
		    		int diff =(int) (mScroller.getCurrY()-mLastScroll);
		    		Log.v("mScroller", mScroller.getCurrY()+"");
		    		if (diff >= LastDiff) {
		    			Scroll=8;
		    		} else {
		    			Scroll=-8;
		    		}
		    		setText=hourConteiner.getScrollY();
		    		hourConteiner.scrollBy(0, Scroll);
		    		Log.v("hour", hourConteiner.getScrollY()+"");
		    		LastDiff = diff;
		    		mScroller.computeScrollOffset();
		    		return true;
		    	}
		    	
		    });
		    dialog.show();
	}

	@Override
	 public void onClick(View v) {
		  switch(v.getId()) {
		  case R.id.hours_plus:
			  if (hour<23) {
				  hour++;
			  } else {
				  hour = 0;
				  
			  }
			  pickerHour.setText(hour+"");
			  pickerHour.clearFocus();
			  pickerMinute.clearFocus();
			  break;
		  case R.id.hours_minus:
			  if (hour<1) {
				  hour = 23;
			  } else {
				  hour--;
			  }
			  pickerHour.setText(hour+"");
			  pickerHour.clearFocus();
			  pickerMinute.clearFocus();
			  break;
		  case R.id.minute_plus:
			  if (minute<59) {
				  minute++;
			  } else {
				  minute = 0;
			  }
			  pickerMinute.setText(minute+"");
			  pickerHour.clearFocus();
			  pickerMinute.clearFocus();
			  break;
		  case R.id.minute_minus:
			  if (minute<1) {
				  minute = 59;
			  } else {
				  minute --;
			  }
			  pickerMinute.setText(minute+"");
			  pickerHour.clearFocus();
			  pickerMinute.clearFocus();
			  break;
		  case R.id.SetTime:
			  if (pickerHour.getText().length() == 0) {
				  hour = 0;
			  } else {
				  hour = Integer.parseInt(pickerHour.getText().toString());
			  }
			  if(pickerMinute.getText().length() == 0) {
				  minute = 0;
			  } else {
				  minute = Integer.parseInt(pickerMinute.getText().toString());
			  }
			  timerHour.setText(hour+"");
			  timerMinute.setText(minute+"");
			  dialog.dismiss();
			  break;
		  }
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public float dpToPx(int dp) {
		Resources r = dialog.getContext().getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }
	
	public void setText(int set, EditText timer) {
		for (int i=0, length = Math.abs(set); i<length; i++) {
			if(set>0){
				if(hour == 0) {
					hour = 23;
				} else {
					hour--;
				}
			} else if (set<0) {
				if(hour == 23) {
					hour = 0;
				} else {
					hour++;
				}
			}
		}
		timer.setText(hour+"");
	}
	
	public void actionUp() {
		hourConteiner.removeView(hourBig);
		hourPlus.setVisibility(View.VISIBLE);
		for(int i=0; i<22; i++) {
		hourConteiner.removeView(hourSmall[i]);
		}
		hourMinus.setVisibility(View.VISIBLE);
		checker = false;
	}
	
	public void isPressedButton() {
		if (hourPlus.isPressed()) {
			hourPlus.setPressed(false);
		}
		if (hourMinus.isPressed()) {
			hourMinus.setPressed(false);
		}
		if (minutePlus.isPressed()) {
			minutePlus.setPressed(false);
		}
		if (minuteMinus.isPressed()) {
			minuteMinus.setPressed(false);
		}
	}

}
