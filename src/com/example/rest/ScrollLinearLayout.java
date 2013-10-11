package com.example.rest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.text.InputType;
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
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

@SuppressLint("NewApi")
public class ScrollLinearLayout extends LinearLayout implements OnClickListener,OnTouchListener{
	
	private Scroller mScroller;
	
	private Button plus;
	
	private Button minus;
	
	private EditText picker;
	
	private OnScrollListener listener;
	
	private VelocityTracker mVelocityTracker;
	
	private int time;
	private int maxTime;
	
	private int mLastY, mScrollY, y, diffY;
	
	private int setId, setIntPlus, setIntMinus;
	
	private boolean checkerDiffY = false, checker = true;
	
	final private Drawable shapeTimer;
	
	final private int HeightView = (int) dpToPx(50);
	final private int WidthView = (int) dpToPx(50);
	final private LayoutParams layoutParams = new LayoutParams(WidthView, HeightView);
	final private int TextSize = 25;
	final private int indent = (int) dpToPx(10);
	
	private static final int plusId = -1;
	private static final int pickerId = -2;
	private static final int minusId = -3;
	

	public ScrollLinearLayout(final Context Context, Integer Time, Integer MaxTime) {
		super(Context);
		mScroller = new Scroller(Context);
		setGravity(Gravity.CENTER);
		setOrientation(VERTICAL);
		setOverScrollMode(OVER_SCROLL_ALWAYS);
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
		picker.setInputType(InputType.TYPE_CLASS_NUMBER);
		picker.setBackgroundDrawable(shapeTimer);
		picker.setId(pickerId);
		picker.setFocusable(false);
		picker.setOnTouchListener(this);
		
		onTimePickerListener pickerListener = new onTimePickerListener(maxTime){

			@Override
			public String getText() {
				// TODO Auto-generated method stub
				return picker.getText().toString();
			}

			@Override
			public void setText(int set) {
				// TODO Auto-generated method stub
				picker.setText(set+"");
			}

			@Override
			public Object getDialogInput() {
				// TODO Auto-generated method stub
				return getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			}

			@Override
			public IBinder getViewIBinder() {
				// TODO Auto-generated method stub
				return getApplicationWindowToken();
			}

			@Override
			public void focusable(boolean seter) {
				// TODO Auto-generated method stub
				picker.setFocusable(seter);
			}

			@Override
			void focusableTouch(boolean seter) {
				// TODO Auto-generated method stub
				picker.setFocusableInTouchMode(seter);
				picker.setFocusable(seter);
			}
			
		};
		picker.setOnLongClickListener(pickerListener);
		picker.setOnFocusChangeListener(pickerListener);
		picker.setOnKeyListener(pickerListener);
		
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
		
		addView(plus, plusId);
		addView(picker, pickerId);
		addView(minus, minusId);
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
				time = plusTime(time);
				picker.setText(time+"");
				break;
			case minusId:
				time = minusTime(time);
				picker.setText(time+"");
				break;
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		final int action = event.getAction();
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
				y = (int) event.getRawY();
				setIntPlus = time;
				setIntMinus = time;
				return false;
			case MotionEvent.ACTION_MOVE:
				diffY = y - (int) event.getRawY();
				Log.v("diffY", diffY+"");
				if(Math.abs(diffY)>3) {
					checkerDiffY = true;
				}
				if (checkerDiffY){
					mScrollY = diffY - mLastY;
					mLastY = diffY;
					
					removeView(plus);
					removeView(minus);
					
					EditText pickerForScroll[] = new EditText[4];
					for(int i = 0; i < 4; i++) {
						pickerForScroll[i] = new EditText(getContext());
						pickerForScroll[i].setLayoutParams(layoutParams);
						pickerForScroll[i].setBackgroundDrawable(shapeTimer);
						pickerForScroll[i].setTextSize(TextSize);
						pickerForScroll[i].setGravity(Gravity.CENTER);
						pickerForScroll[i].setTextColor(Color.BLACK);
						if (i % 2 == 0) {
							setIntPlus = plusTime(setIntPlus);
							pickerForScroll[i].setText(setIntPlus+"");
							setId = plusId+1;
							addView(pickerForScroll[i],setId);
						} else {
							setIntMinus = minusTime(setIntMinus);
							pickerForScroll[i].setText(setIntMinus+"");
							setId = minusId - 1;
							addView(pickerForScroll[i],setId);
						}
					}
					
					EditText pickerBig = new EditText(getContext());
					pickerBig.setLayoutParams(layoutParams);
					pickerBig.setBackgroundDrawable(shapeTimer);
					pickerBig.setGravity(Gravity.CENTER);
					pickerBig.setTextSize(TextSize);
					pickerBig.setTextColor(Color.BLACK);
					
					if(Math.abs(diffY)%HeightView==0) {
						if (mScrollY < 0) {
							setIntPlus = plusTime(setIntPlus);
							setId = plusId+1;
							pickerBig.setText(setIntPlus+"");
							addView(pickerBig,setId, new LayoutParams(0,0));
						} 
						if (mScrollY > 0) {
							setIntMinus = minusTime(setIntMinus);
							setId  = minusId-1;
							pickerBig.setText(setIntMinus+"");
							addView(pickerBig,setId, new LayoutParams(0,0));
						}
					}
					
				}
					
					scrollBy(0,mScrollY);
					
					
				return true;
			case MotionEvent.ACTION_UP:
				double scrollY = (double) getScrollY()/HeightView;
				int seter = returnTrueTime(scrollY);
				setText(seter,picker);
				CleanAll();
				mLastY = 0;
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int initialVelocity = (int) velocityTracker.getYVelocity();
				
				fling(-initialVelocity);
				if(checkerDiffY) {
					checkerDiffY = false;
					return true;
				}
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
	
	public int plusTime(int nowTime) {
		if(nowTime == maxTime) {
			nowTime = 0;
		} else {
			nowTime++;
		}
		return nowTime;
	}
	
	public int minusTime(int nowTime) {
		if (nowTime == 0) {
			nowTime = maxTime;
		} else {
			nowTime--;
		}
		return nowTime;
	}
	
	public interface OnScrollListener {
		public void onScroll(long x);
	}

	public int returnTrueTime(double seter) {
		int result = 0;
		if(seter >= 0 && seter % 1 >= 0.5) {
			result = (int) seter+1;
		}
		if(seter > 0 && seter % 1 < 0.5) {
			result = (int) seter;
		}
		if(seter <= 0 && seter % 1 >= 0.5) {
			result = (int) seter-1;
		} 
		if (seter < 0 && seter % 1 < 0.5) {
			result = (int) seter-1;
		}
		return result;
	}

	public void setText(int set, EditText timer) {
		for (int i=0, length = Math.abs(set); i<length; i++) {
			if(set>0){
				if(time == 0) {
					time = maxTime;
				} else {
					time--;
				}
			} else if (set<0) {
				if(time == maxTime) {
					time = 0;
				} else {
					time++;
				}
			}
		}
		timer.setText(time+"");
	}
	
	public void CleanAll() {
		scrollTo(0,0);
		removeAllViews();
		picker.setText(time+"");
		addView(plus,plusId);
		addView(picker,pickerId);
		addView(minus,minusId);
		
	}
	
	public void isPressedButton() {
		if (plus.isPressed()) {
			plus.setPressed(false);
		}
		if (minus.isPressed()) {
			minus.setPressed(false);
		}
	}
}