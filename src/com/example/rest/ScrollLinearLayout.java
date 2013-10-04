package com.example.rest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class ScrollLinearLayout extends LinearLayout implements OnClickListener,OnTouchListener{
	
	private Scroller mScroller;
	
	private Button plus;
	
	private Button minus;
	
	private EditText picker;
	
	private OnScrollListener listener;
	
	private VelocityTracker mVelocityTracker;
	
	private int time;
	private int maxTime;
	
	private int mLastY, mScrollY;
	
	final private Drawable shapeTimer;
	
	final private int HeightView = (int) dpToPx(50);
	final private int WidthView = (int) dpToPx(50);
	final private LayoutParams layoutParams = new LayoutParams(WidthView, HeightView);
	final private int TextSize = 25;
	final private int indent = (int) dpToPx(10);
	
	private static final int plusId = -1;
	private static final int pickerId = -2;
	private static final int minusId = -3;
	

	public ScrollLinearLayout(Context Context, Integer Time, Integer MaxTime) {
		super(Context);
		mScroller = new Scroller(Context);
		setGravity(Gravity.CENTER);
		setOrientation(VERTICAL);
		setLayoutParams(new LayoutParams(WidthView+indent, 3*HeightView));
		
		time = Time;
		maxTime = MaxTime;
		
		shapeTimer = getContext().getResources().getDrawable(R.drawable.text_for_timer);
		
		plus = new Button(Context);
		plus.setLayoutParams(layoutParams);
		Drawable plusShape = getContext().getResources().getDrawable(R.drawable.button_up_selector);
		plus.setGravity(Gravity.CENTER);
		plus.setBackgroundDrawable(plusShape);
		plus.setId(plusId);
		plus.setOnClickListener(this);
		plus.setOnTouchListener(this);
		
		picker = new EditText(Context);
		picker.setLayoutParams(layoutParams);
		//picker.setHeight((int) dpToPx(HeightView));
		//picker.setWidth((int) dpToPx(WidthView));
		picker.setTextSize(TextSize);
		picker.setTextColor(Color.BLACK);
		picker.setGravity(Gravity.CENTER);
		picker.setText(Time+"");
		picker.setBackgroundDrawable(shapeTimer);
		picker.setId(pickerId);
		picker.setFocusable(false);
		picker.setOnTouchListener(this);
		
		minus = new Button(Context);
		minus.setLayoutParams(layoutParams);
		//minus.setHeight((int) dpToPx(HeightView));
		//minus.setWidth((int) dpToPx(WidthView));
		Drawable minusShape = getContext().getResources().getDrawable(R.drawable.button_down_selector);
		minus.setGravity(Gravity.CENTER);
		minus.setBackgroundDrawable(minusShape);
		minus.setId(minusId);
		minus.setOnClickListener(this);
		minus.setOnTouchListener(this);
		
		addView(plus);
		addView(picker);
		addView(minus);
	}

	@Override
	public void onClick(View v) {
		picker.clearFocus();
		String setTime = picker.getText().toString();
		if (setTime.length() > 0) {
			time = Integer.parseInt(setTime);
		}
		switch(v.getId()) {
			case plusId:
				if(time == maxTime) {
					time = 0;
				} else {
					time++;
				}
				picker.setText(time+"");
				break;
			case minusId:
				if(time == 0) {
					time = maxTime;
				} else {
					time--;
				}
				picker.setText(time+"");
				break;
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		final int action = event.getAction();
		final int y = (int) event.getRawY();
		if (action == MotionEvent.ACTION_DOWN) {
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
		}
		
		if(mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		
		switch(action) {
			case MotionEvent.ACTION_DOWN:
				return false;
			case MotionEvent.ACTION_MOVE:
				mScrollY = mLastY - y;
				scrollBy(0,mScrollY);
			case MotionEvent.ACTION_UP:
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int initialVelocity = (int) velocityTracker.getYVelocity();
				fling(-initialVelocity);
		}
		return false;
	}
	/*
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()) {
			mScrollY = mScroller.getCurrY();
			Log.v("fsdf", mScrollY+"");
			super.scrollBy(0,mScrollY);
			postInvalidate();
		}
	}
	*/
	private void fling(int velocityY) {
		mScroller.fling(0, mScrollY, 0, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
		invalidate();
	}
	
	public void setOnScrollListener(OnScrollListener l) {
		listener = l;
	}
	
	public float dpToPx(int dp) {
		Resources r = getContext().getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }
	
	public void setText() {
		String getTime = picker.getText().toString();
		if (getTime.length() == 0) {
			time = 0;
		}
	}
	
	public interface OnScrollListener {
		public void onScroll(long x);
	}


}
