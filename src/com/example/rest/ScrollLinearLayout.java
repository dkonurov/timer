package com.example.rest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.Scroller;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

@SuppressLint("NewApi")
public class ScrollLinearLayout extends LinearLayout implements OnClickListener,OnTouchListener{

    final private Drawable shapeTimer;//freeze now, plusShape, minusShape;

    final private int HeightView = (int) dpToPx(50);
    final private int WidthView = (int) dpToPx(50);
    final private LayoutParams layoutParams = new LayoutParams(WidthView, HeightView);
    final private int TextSize = 25;
    final private int indent = (int) dpToPx(10);

    private static final int plusId = -1;
    private static final int pickerId = -2;
    private static final int minusId = -3;
    private static final int speed = 500;
    private static final int helpVelocity = 5;

    private final int maxScroll, minScroll;

    private Scroller mScroller;

    private EditText pickerForScroll[];

    private EditText picker;

    private VelocityTracker mVelocityTracker;

    private int time;
    private int maxTime;

    private int indentScroll;

    private int mLastY, mScrollY, y, diffY, saveScroll, saveScrollPos = 0;


    private int initialVelocity = 0;

    private boolean checkerDiffY = false, checkerCorrectedScroll = false;

	private int mMinimumVelocity, touchSlop;




    public ScrollLinearLayout(final Context Context, Integer Time, Integer MaxTime) {
        super(Context);
        mScroller = new Scroller(getContext(), new DecelerateInterpolator(8f));
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(WidthView+indent, 3*HeightView));

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        touchSlop = configuration.getScaledTouchSlop();

        time = Time;
        maxTime = MaxTime;

        shapeTimer = getContext().getResources().getDrawable(R.drawable.text_for_timer);

        picker = initialEditText();
        picker.setText(time+"");

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

            @Override
            boolean check() {
                // TODO Auto-generated method stub
                return checkerDiffY;
            }

        };
        picker.setOnLongClickListener(pickerListener);
        picker.setOnFocusChangeListener(pickerListener);
        picker.setOnKeyListener(pickerListener);
        
        pickerForScroll = new EditText[maxTime+1];
        int seterForScroll = time;
        for (int i = 0, length = maxTime+1; i<length; i++) {
            pickerForScroll[i] = initialEditText();
            pickerForScroll[i].setText(seterForScroll+"");
            seterForScroll = plusTime(seterForScroll);
            pickerForScroll[i].setLongClickable(false);
            if (i == 0) {
            	addView(pickerForScroll[i], pickerId);
            } else {
            addView(pickerForScroll[i], minusId);
            }
        }
        
        scrollTo(0,-(HeightView)*(maxTime)/2);
        
        maxScroll = (HeightView*maxTime/2);
        minScroll = (-(HeightView)*maxTime/2);
    }

    @Override
    public void onClick(View v) {
    	clearFocus();
    	int minusTime = minusTime(time);
    	int plusTime = plusTime(time);
    	if (v == pickerForScroll[plusTime]) {
    		time = plusTime(time);
    		mScroller.fling(0, getScrollY(),0, speed, 0, 0, minScroll, getScrollY()+HeightView);
    		scrollBy(0,HeightView/10);
    	}
    	if (v == pickerForScroll[minusTime]) {
    		time = minusTime(time);
    		mScroller.fling(0,getScrollY(),0, -speed,0,0,getScrollY()-HeightView,getScrollY());
    		scrollBy(0, -HeightView);
    	}
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        
        if (!mScroller.isFinished())  {
        	mScroller.abortAnimation();
        	correctedScroll();
        }
        
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                saveScroll -= 2*saveScrollPos;
                y = (int) event.getRawY();
                return false;
            case MotionEvent.ACTION_MOVE:
                diffY = y - (int) event.getRawY();
                if(Math.abs(diffY)>touchSlop) {
                    checkerDiffY = true;
                }
                if (checkerDiffY){
                    mScrollY = diffY - mLastY;
                    mLastY = diffY;
                    
                    scrollBy(0,mScrollY);
                    saveScroll += mScrollY;
                }

                return true;
            case MotionEvent.ACTION_UP:
            	if (getScrollY() > maxScroll) {
            		scrollBy(0,maxScroll-getScrollY());
            	}
            	if (getScrollY() < minScroll) {
            		scrollBy(0,minScroll-getScrollY());
            	}
            	
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(100);
                initialVelocity = (int) velocityTracker.getYVelocity();
                if (Math.abs(initialVelocity) < mMinimumVelocity) {
                	correctedScroll();
                } else {
	                initialVelocity = initialVelocity*helpVelocity;
	                mScroller.fling(0, getScrollY(), 0, -initialVelocity, 0, 0, minScroll,maxScroll);
	                if (initialVelocity > 0) {
	                	scrollBy(0, -1);
	                } else if (initialVelocity < 0) {
	                	scrollBy(0, 1);
	                }
                }
            	
                if(checkerDiffY) {
                	
                    mLastY = 0;
                    checkerDiffY = false;
                    return true;
                }

        }
        return false;
    }

    public float dpToPx(int dp) {
        Resources r = getContext().getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
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

    public int returnTrueTime(double seter) {
        int result = 0;
        if(seter >= 0 && seter % 1 >= 0.5) {
            result = (int) seter+1;
        }
        if(seter > 0 && seter % 1 < 0.5) {
            result = (int) seter;
        }
        if(seter <= 0 && seter % 1 >= -0.5) {
            result = (int) seter;
        }
        if (seter < 0 && seter % 1 < -0.5) {
            result = (int) seter-1;
        }
        return result;
    }

    private void setText(int set) {
    	time = 0;
        for (int i=0, length = Math.abs(set); i<length; i++) {
            if(set<0){
            	time = minusTime(time);
            } else if (set>0) {
            	time = plusTime(time);
            }
        }
    }

    private void endScroll () {
    	double setScroll = (double) getScrollY() - minScroll;
        double scrollY = (double) setScroll / HeightView;
        int seter = returnTrueTime(scrollY);
        setText(seter);
        checkerCorrectedScroll = false;
    }

    private void correctedScroll() {
    	
        int setScroll = getScrollY()-minScroll;
        int finder = (int) setScroll%(HeightView);
        if (finder<HeightView/2) {
        	finder *=-1;
        } else {
            int seter = HeightView - finder;
            finder = seter;
        }
        if (finder != 0) {
	        			if (finder > 0) {
	        				if (!checkerCorrectedScroll) {
	        				mScroller.fling(0, getScrollY(), 0, speed, 0, 0, getScrollY(), getScrollY()+finder);
	        				}
		        			scrollBy(0,1);
	        			} else {
	        				if (!checkerCorrectedScroll) {
	        				mScroller.fling(0, getScrollY(), 0, -speed, 0, 0, getScrollY(), getScrollY()+finder);
	        				}
	        				scrollBy(0,-1);
	        		}
        } else {
        	endScroll();
        }
        checkerCorrectedScroll = true;

    }

	public Integer getTime () {
        return time;
    }

    public EditText initialEditText() {
        EditText scrollEditText = new EditText(getContext());
        scrollEditText.setLayoutParams(layoutParams);
        scrollEditText.setBackgroundDrawable(shapeTimer);
        scrollEditText.setFocusable(false);
        scrollEditText.setTextSize(TextSize);
        scrollEditText.setGravity(Gravity.CENTER);
        scrollEditText.setTextColor(Color.BLACK);
        scrollEditText.setOnTouchListener(this);
        scrollEditText.setOnClickListener(this);
        return scrollEditText;
    }
    
    @Override
	public void computeScroll() {
    	if (mScroller.computeScrollOffset()) {
    		mScrollY = mScroller.getCurrY();
    		mScrollY = mScrollY - getScrollY();
    		if (mScrollY == 0) {
    			correctedScroll();
    		} else {
    			scrollBy(0,mScrollY);
    		}
    	}
    	if (mScroller.isFinished()) {
    		if (!checkerDiffY) {
    			correctedScroll();
    		}
    	}
    }
    
    public void setValues(String[] formatter) {
    	for (int i = 0, length = formatter.length; i < length; i++) {
    		pickerForScroll[i].setText(formatter[i]);
    	}
    }
}