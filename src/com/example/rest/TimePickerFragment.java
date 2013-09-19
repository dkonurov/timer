package com.example.rest;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.TextView;

@SuppressLint({ "NewApi", "ValidFragment" })
public class TimePickerFragment extends DialogFragment implements OnClickListener, android.view.View.OnClickListener {
	
	protected static final String LOG_TAG = "myLog";
	public EditText timerHour;
	public EditText timerMinute;
	
	public TextView[] hourSmall = new TextView[22];
	public TextView hourBig;
	
	public View myView;
	
	public int setText = 0;
	
	public int Scroll = 0;
	
	public int mLastScroll = 0;
	
	public EditText pickerHour;
	public EditText pickerMinute;
	
	public OverScroller mScroller;
	
	public OnTouchListener mGestureListener;
	
	private boolean checker = false;
	
	public LinearLayout hourConteiner;
	public LinearLayout minuteConteiner;
	
	public int LastDiff = 0;
	
	public VelocityTracker mVelocityTracker = null;
	
	 final GestureDetector gesture;
	
	public Button hourPlus;
	public Button minutePlus;
	public Button hourMinus;
	public Button minuteMinus;
	public Button setTime;

	public Integer hour;
	public Integer minute;
	
	public  TimePickerFragment(EditText view1, EditText view2, int setHours, int setMinute) {
		timerHour = view1;
		timerMinute = view2;
		hour = setHours;
		minute = setMinute;
		
		gesture = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){
	    	
	    	@Override
	    	public boolean onDown(MotionEvent e) {
	    		
	    		return false;
	    	}
	    	
	    	@Override
	    	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
	    		
	    		hourConteiner.scrollTo(0, 0);
	    		int set = setText/40;
	    		setText(set, pickerHour);
	    		Log.v("onFling", set+"");
	    		setText = 0;
	    		actionUp();
	    		return true;
	    	}
	    	
	    	@Override
	    	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	    		if(!checker) {
	    			Drawable shape = getResources().getDrawable(R.drawable.text_for_timer);
	    			int forHourBig;
	    			if (hour == 23) {
	    				forHourBig = 0;
	    			} else {
	    				forHourBig = hour+1;
	    			}
	    			hourBig.setText(forHourBig+"");
	    			hourBig.setTextSize(25);
	    			hourBig.setHeight(dpToPx(50));
	    			hourBig.setWidth(dpToPx(50));
	    			hourBig.setBackground(shape);
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
	    				hourSmall[i].setHeight(dpToPx(50));
	    				hourSmall[i].setWidth(dpToPx(50));
	    				hourSmall[i].setBackground(shape);
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
	    		int diff =(int) (e2.getY() - e1.getY());
	    		
	    		if (diff >= LastDiff) {
	    			Scroll=8;
	    		} else {
	    			Scroll=-8;
	    		}
	    		setText+=Scroll;
	    		Log.v("setText", setText+"");
	    		Log.v("diff", diff+"");
	    		Log.v("mLastdiff", LastDiff+"");
	    		hourConteiner.scrollBy(0, Scroll);
	    		LastDiff = diff;
	    		return true;
	    	}
	    	
	    });
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		    
		    getDialog().getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		    View v = inflater.inflate(R.layout.fragment_time_picker, null);
		    
		    mScroller = new OverScroller(getDialog().getContext());
		    
		    setTime = (Button) v.findViewById(R.id.SetTime);
		    hourPlus = (Button) v.findViewById(R.id.hours_plus);
		    hourMinus = (Button) v.findViewById(R.id.hours_minus);
		    minutePlus = (Button) v.findViewById(R.id.minute_plus);
		    minuteMinus = (Button) v.findViewById(R.id.minute_minus);
		    
		    for (int i=0; i<22; i++)
    		hourSmall[i] = new TextView(getDialog().getContext());
    		hourBig = new TextView(getDialog().getContext());
		    
		    hourConteiner = (LinearLayout) v.findViewById(R.id.hours_scroll);

		    minuteConteiner = (LinearLayout) v.findViewById(R.id.minute_container);
		    
		    	mGestureListener = new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if(gesture.onTouchEvent(arg1)) {
						return true;
					}
					if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Log.v("ACTION", "ACTION UP!!!");
						if (checker) {
							int set = setText/40;
							setText(set, pickerHour);
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
		    
		    pickerHour = (EditText) v.findViewById(R.id.hours_dislpay);
		    pickerMinute = (EditText) v.findViewById(R.id.minute_display);
		    
		    pickerHour.setOnClickListener(this);
		    pickerMinute.setOnClickListener(this);
		    
		    pickerHour.setFocusable(false);
		    pickerMinute.setFocusable(false);
		    
		    pickerHour.setText(hour+"");
		    pickerMinute.setText(minute+"");
		    
		    pickerHour.setId(1);
		    pickerMinute.setId(2);
		    
		    pickerHour.setOnKeyListener(new OnKeyListener(){

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_DOWN && arg1 != KeyEvent.KEYCODE_DEL) {
						if (pickerHour.getText().length() != 0 ) {
							int set = Integer.parseInt(arg1+"")-7;
							int checkHours = Integer.parseInt(pickerHour.getText().toString());
							if (checkHours*10 + set > 23) {
								return true;
							} 
						}
					}
					return false;
				}
		    	
		    });
		    
		    pickerMinute.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode != KeyEvent.KEYCODE_DEL) {
						if (pickerMinute.getText().length() != 0) {
							int set = Integer.parseInt(keyCode+"")-7;
							int checkMinute = Integer.parseInt(pickerMinute.getText().toString());
							if (checkMinute*10 + set > 59) {
								return true;
							}
						}
					}
					return false;
				}
		    	
		    });
		    
		    pickerHour.setOnFocusChangeListener(new OnFocusChangeListener() {          

		        public void onFocusChange(View v, boolean hasFocus) {
		            if(!hasFocus) {
		            	if (pickerHour.getText().length() == 0) {
		            		pickerHour.setText("0");
		            	}
		            	pickerHour.setFocusable(false);
		            	InputMethodManager inputMethodManager = (InputMethodManager) getDialog().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(getView().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		            	
		            }
		               //do job here owhen Edittext lose focus 
		        }
		    });
		    
		    pickerMinute.setOnFocusChangeListener(new OnFocusChangeListener() {          

		        public void onFocusChange(View v, boolean hasFocus) {
		            if(!hasFocus) {
		            	if (pickerMinute.getText().length() == 0) {
		            		pickerHour.setText("0");
		            	}
		            	pickerMinute.setFocusable(false);
		            }
		               //do job here owhen Edittext lose focus 
		        }
		    });
		    
		    pickerHour.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					pickerHour.setFocusableInTouchMode(true);
					pickerHour.setFocusable(true);
					InputMethodManager inputMethodManager = (InputMethodManager) getDialog().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.toggleSoftInputFromWindow(getView().getApplicationWindowToken(), InputMethodManager.SHOW_FORCED,0);
					int selectionIndex = pickerHour.length();
					Log.d(LOG_TAG, selectionIndex+"");
					pickerHour.setSelection(selectionIndex, selectionIndex);
					return false;
				}
		    	
		    });
		    
		    pickerMinute.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					pickerMinute.setFocusable(true);
					pickerMinute.setFocusableInTouchMode(true);
					InputMethodManager inputMethodManager = (InputMethodManager) getDialog().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.toggleSoftInputFromWindow(getView().getApplicationWindowToken(), InputMethodManager.SHOW_FORCED,0);
					int selectionIndex = pickerMinute.length();
					pickerMinute.setSelection(selectionIndex, selectionIndex);
					return false;
				}
		    	
		    });
		    myView = v;
		    return v;
		  }

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
				  dismiss();
				  break;
			  }
		  }
			  

		  public void onDismiss(DialogInterface dialog) {
		    super.onDismiss(dialog);
		  }

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			
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
		
		public int dpToPx(int dp) {
	        DisplayMetrics displayMetrics = getDialog().getContext().getResources().getDisplayMetrics();
	        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
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
}