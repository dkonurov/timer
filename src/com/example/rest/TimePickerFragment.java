package com.example.rest;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint({ "NewApi", "ValidFragment" })
public class TimePickerFragment extends DialogFragment implements OnClickListener, android.view.View.OnClickListener {
	
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
	public  TimePickerFragment(EditText view1, EditText view2, int setHours, int setMinute) {
		timerHour = view1;
		timerMinute = view2;
		hour = setHours;
		minute = setMinute;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		    
		    getDialog().getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
		    View v = inflater.inflate(R.layout.fragment_time_picker, null);
		    
		    
		    setTime = (Button) v.findViewById(R.id.SetTime);
		    hourPlus = (Button) v.findViewById(R.id.hours_plus);
		    hourMinus = (Button) v.findViewById(R.id.hours_minus);
		    minutePlus = (Button) v.findViewById(R.id.minute_plus);
		    minuteMinus = (Button) v.findViewById(R.id.minute_minus);
		    
		    setTime.setOnClickListener(this);
		    hourPlus.setOnClickListener(this);
		    hourMinus.setOnClickListener(this);
		    minutePlus.setOnClickListener(this);
		    minuteMinus.setOnClickListener(this);
		    
		    pickerHour = (EditText) v.findViewById(R.id.hours_dislpay);
		    pickerMinute = (EditText) v.findViewById(R.id.minute_display);
		    
		    pickerHour.setOnClickListener(this);
		    pickerMinute.setOnClickListener(this);
		    
		    
		    pickerHour.setText(hour+"");
		    pickerMinute.setText(minute+"");
		    
		    pickerHour.setOnKeyListener(new OnKeyListener(){

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if (arg2.getAction() == KeyEvent.ACTION_UP) {
						if (pickerHour.getText().length() != 0 ) {
							String pickerisHour = pickerHour.getText().toString();
							int checkHours = Integer.parseInt(pickerisHour);
							if (checkHours > 23) {
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
		            }
		               //do job here owhen Edittext lose focus 
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
				  break;
			  case R.id.hours_minus:
				  if (hour<1) {
					  hour = 23;
				  } else {
					  hour--;
				  }
				  pickerHour.setText(hour+"");
				  break;
			  case R.id.minute_plus:
				  if (minute<59) {
					  minute++;
				  } else {
					  minute = 0;
				  }
				  pickerMinute.setText(minute+"");
				  break;
			  case R.id.minute_minus:
				  if (minute<1) {
					  minute = 59;
				  } else {
					  minute --;
				  }
				  pickerMinute.setText(minute+"");
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
}