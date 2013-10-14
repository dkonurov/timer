package com.example.rest;

import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;

public abstract class onTimePickerListener implements OnKeyListener, OnFocusChangeListener, OnLongClickListener {
	
	private int maxTime;
	
	public onTimePickerListener(int setTime){
		maxTime = setTime;
	}

	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		if (arg2.getAction() == KeyEvent.ACTION_DOWN && arg1 != KeyEvent.KEYCODE_DEL) {
			if (getText().length() != 0 ) {
				int set = Integer.parseInt(arg1+"")-7;
				int checkHours = Integer.parseInt(getText());
				if (checkHours*10 + set > maxTime) {
					return true;
				} 
			}
		}
		return false;
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(!hasFocus) {
        	if (getText().length() == 0) {
        		setText(0);
        	}
        	focusable(false);
        	InputMethodManager inputMethodManager = (InputMethodManager) getDialogInput();
			inputMethodManager.hideSoftInputFromWindow(getViewIBinder(), InputMethodManager.HIDE_NOT_ALWAYS);
        	
        }
	}
	
	@Override
	public boolean onLongClick(View arg0) {
		// TODO Auto-generated method stub
		if (!check()){
			focusableTouch(true);
			InputMethodManager inputMethodManager = (InputMethodManager) getDialogInput();
			inputMethodManager.toggleSoftInputFromWindow(getViewIBinder(), InputMethodManager.SHOW_FORCED,0);
		}
		return false;
	}

	abstract public String getText();
	
	abstract public void setText(int set);
	
	abstract public Object getDialogInput();
	
	abstract public IBinder getViewIBinder();
	
	abstract public void focusable(boolean seter);
	
	abstract void focusableTouch(boolean seter);
	
	abstract boolean check();
}
