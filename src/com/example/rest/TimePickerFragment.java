package com.example.rest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

@SuppressLint({ "NewApi", "ValidFragment" })
public class TimePickerFragment extends DialogFragment implements OnClickListener, android.view.View.OnClickListener, OnGestureListener {
	
	protected static final String LOG_TAG = "myLog";
	public EditText timerHour;
	public EditText timerMinute;
	
	public EditText pickerHour;
	public EditText pickerMinute;
	
	public Button hourPlus;
	public Button minutePlus;
	public Button hourMinus;
	public Button minuteMinus;
	public Button setTime;
	
	public Integer hour;
	public Integer minute;
	
	public GestureDetector gestureDetector;
	
	public  TimePickerFragment(EditText view1, EditText view2, int setHours, int setMinute) {
		timerHour = view1;
		timerMinute = view2;
		hour = setHours;
		minute = setMinute;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		    
		    gestureDetector = new GestureDetector(getDialog().getContext(), new MyGestureListener());
		    
		    getDialog().getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		    View v = inflater.inflate(R.layout.fragment_time_picker, null);
		    
		    
		    setTime = (Button) v.findViewById(R.id.SetTime);
		    hourPlus = (Button) v.findViewById(R.id.hours_plus);
		    hourMinus = (Button) v.findViewById(R.id.hours_minus);
		    minutePlus = (Button) v.findViewById(R.id.minute_plus);
		    minuteMinus = (Button) v.findViewById(R.id.minute_minus);
		    
		    LinearLayout hourConteiner = (LinearLayout) v.findViewById(R.id.hours_conteiner);
		    LinearLayout minuteConteiner = (LinearLayout) v.findViewById(R.id.minute_container);
		    
		    
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
					if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
						if (pickerHour.getText().length() != 0 ) {
							Log.d(LOG_TAG, (char)arg1+"");
							int set = 0;
							String pickerisHour = pickerHour.getText().toString();
							int checkHours = Integer.parseInt(pickerisHour);
							switch (arg1) {
								case KeyEvent.KEYCODE_NUMPAD_0:
									set =0;
									Log.d(LOG_TAG, "press 1");
									break;
								case KeyEvent.KEYCODE_NUMPAD_1:
									set = 1;
									break;
								case KeyEvent.KEYCODE_NUMPAD_2:
									set = 2;
									break;
								case KeyEvent.KEYCODE_NUMPAD_3:
									set = 3;
									break;
								case KeyEvent.KEYCODE_NUMPAD_4:
									set = 4;
									break;
								case KeyEvent.KEYCODE_NUMPAD_5:
									set = 5;
									break;
								case KeyEvent.KEYCODE_NUMPAD_6:
									set = 6;
									break;
								case KeyEvent.KEYCODE_NUMPAD_7:
									set = 7;
									break;
								case KeyEvent.KEYCODE_NUMPAD_8:
									set = 8;
									break;
								case KeyEvent.KEYCODE_NUMPAD_9:
									set = 9;
									break;
							}
							if (checkHours + set > 23) {
								pickerHour.setText(pickerisHour.substring(0, 1));
								pickerHour.setSelection(1,1);
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
					if (event.getAction() == KeyEvent.ACTION_UP) {
						if (pickerMinute.getText().length() != 0) {
							String pickerisMinute = pickerMinute.getText().toString();
							int checkMinute = Integer.parseInt(pickerisMinute);
							if (checkMinute > 59) {
								pickerMinute.setText(pickerisMinute.substring(0,1));
								pickerMinute.setSelection(1,1);
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
						inputMethodManager.hideSoftInputFromWindow(getView().getApplicationWindowToken(), inputMethodManager.HIDE_IMPLICIT_ONLY);
		            	
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
		
		private class MyGestureListener extends SimpleOnGestureListener {
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				Log.d(LOG_TAG, "x ="+(int)distanceX+" y ="+(int)distanceY);
				return true;
				
			}
		}
		
		public boolean onTouchEvent(MotionEvent event)
		    {
		        if (gestureDetector.onTouchEvent(event)) return true;
		        return true;
		    }

		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			Log.d(LOG_TAG, arg2+" "+arg3);
			return false;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			
			Log.d(LOG_TAG, arg2+" "+arg3);
			return false;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
}