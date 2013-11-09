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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;

@SuppressLint("NewApi")
public class ScrollLinearLayout extends LinearLayout implements OnClickListener,OnTouchListener{

    final private Drawable shapeTimer;//freeze now, plusShape, minusShape;

    final private int HeightView = (int) dpToPx(50);
    final private int WidthView = (int) dpToPx(50);
    final private LayoutParams layoutParams = new LayoutParams(WidthView, HeightView);
    final private int TextSize = 25;
    final private int indent = (int) dpToPx(10);

    private static final int pickerId = -2;
    private static final int minusId = -3;
    private static final int speed = 500;
    private static final int helpVelocity = 8;

    private final int maxScroll, minScroll, startTime;

    private Scroller mScroller;

    private EditText pickerForScroll[];

    private EditText picker;

    private VelocityTracker mVelocityTracker;

    private int time;
    private int maxTime;

    private int mLastY, mScrollY, y, diffY, saveScrollPos = 0;


    private int initialVelocity = 0;

    private boolean checkerDiffY = false, checkerCorrectedScroll = false, checkerForCircularFling = false,  circular;

	private int mMinimumVelocity, touchSlop;




    public ScrollLinearLayout(final Context _context, Integer _time, Integer _maxTime, boolean _circular) {
        super(_context);
        mScroller = new Scroller(getContext(), new DecelerateInterpolator(8f));
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(WidthView+indent, 3*HeightView));

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        touchSlop = configuration.getScaledTouchSlop();

        time = _time;
        maxTime = _maxTime;
        circular = _circular;
        startTime = _time;
        
        shapeTimer = getContext().getResources().getDrawable(R.drawable.text_for_timer);

        picker = initialEditText();
        picker.setText(time+"");

        onTimePickerListener pickerListener = new onTimePickerListener(maxTime){

            @Override
            public String getText() {
                // TODO Auto-generated method stub
                return pickerForScroll[time].getText().toString();
            }

            @Override
            public void setText(int set) {
                // TODO Auto-generated method stub
                pickerForScroll[time].setText(set+"");
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
                pickerForScroll[time].setFocusable(seter);
            }

            @Override
            void focusableTouch(boolean seter) {
                // TODO Auto-generated method stub
                pickerForScroll[time].setFocusableInTouchMode(seter);
                pickerForScroll[time].setFocusable(seter);
            }

            @Override
            boolean check() {
                // TODO Auto-generated method stub
                return checkerDiffY;
            }

        };
        
        int length;
        int seterForScroll;
        if (circular) {
        	pickerForScroll = new EditText[maxTime+4];
        	length = maxTime+4;
        	seterForScroll = minusTime(time);
        } else {
        	pickerForScroll = new EditText[maxTime+1];
        	length = maxTime+1;
        	seterForScroll = time; 
        }
        
        for (int i = 0; i<length; i++) {
            pickerForScroll[i] = initialEditText();
            pickerForScroll[i].setText(seterForScroll+"");
            pickerForScroll[i].setTag(seterForScroll);
            seterForScroll = plusTime(seterForScroll);
            pickerForScroll[i].setLongClickable(false);
            if (i == 0) {
            	addView(pickerForScroll[i], pickerId);
            } else {
            	addView(pickerForScroll[i], minusId);
            }
        }
        
        scrollTo(0,-(HeightView)*(maxTime)/2);
        
        if (circular) {
        	super.scrollBy(0, -HeightView/2);
        }
        
        scrollBy(0,time*HeightView);

        if (circular) {
        	maxScroll = (HeightView*maxTime/2+HeightView/2);
        	minScroll = (-(HeightView)*maxTime/2 - HeightView/2);
        } else {
            maxScroll = HeightView*maxTime/2;
            minScroll = -(HeightView)*maxTime/2;
        }
    }
    
    
    @Override
	public void scrollBy(int x, int y) {
    	if (circular) {
    		if (y+getScrollY() > maxScroll) {
    			scrollTo(0, minScroll);
    			checkerForCircularFling = true;
    		} else if (y+getScrollY() < minScroll) {
    			scrollTo(0, maxScroll);
    			checkerForCircularFling = true;
    		}
    	}
    	super.scrollBy(x, y);
    }
    
    @Override
    public void scrollTo(int x, int y) {
    	super.scrollTo(0, y);
    }
	
    @Override
    public void onClick(View v) {
    	clearFocus();
    	int minusTime = minusTime(time);
    	int plusTime = plusTime(time);
    	int tag = (Integer) v.getTag();
    	if (plusTime == tag) {
    		time = plusTime(time);
    		mScroller.fling(0, getScrollY(),0, speed, 0, 0, minScroll, getScrollY()+HeightView);
    		scrollBy(0,HeightView/10);
    	}
    	if (minusTime == tag) {
    		time = minusTime(time);
    		mScroller.fling(0,getScrollY(),0, -speed,0,0,getScrollY()-HeightView,getScrollY());
    		scrollBy(0, -HeightView/10);
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
                y = (int) event.getRawY();
                if (y == maxScroll || y == minScroll) {
                	checkerForCircularFling = true;
                }
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
                }

                return true;
            case MotionEvent.ACTION_UP:
            	if (!circular) {
	            	if (getScrollY() > maxScroll) {
	            		scrollBy(0,maxScroll-getScrollY());
	            	}
	            	if (getScrollY() < minScroll) {
	            		scrollBy(0,minScroll-getScrollY());
	            	}
            	}
            	
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(100);
                initialVelocity = (int) velocityTracker.getYVelocity();
                if (Math.abs(initialVelocity) < mMinimumVelocity) {
                	correctedScroll();
                } else {
	                initialVelocity = initialVelocity*helpVelocity;
	                if (Math.abs(initialVelocity) > 2000) {
	                	while(Math.abs(initialVelocity) > 500) {
	                		initialVelocity /= 4;
	                	}
	                }
	                fling(initialVelocity);
	                if (!checkerForCircularFling) {
		                if (initialVelocity > 0) {
		                	scrollBy(0, -1);
		                } else if (initialVelocity < 0) {
		                	scrollBy(0, 1);
		                }
	                } else {
	                	correctedScroll();
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
    	time = startTime;
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
        checkerForCircularFling = false;
        saveScrollPos = 0;
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
    		if (mScrollY< minScroll) {
    			mScrollY = mScrollY - minScroll - saveScrollPos;
    			saveScrollPos += mScrollY;
    		} else if (mScrollY > maxScroll) {
    			mScrollY = mScrollY - maxScroll - saveScrollPos;
    			saveScrollPos += mScrollY;
    		} else {
    			mScrollY = mScrollY - getScrollY();
    		}
    		
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
    
    public void fling(int velocityY) {
    	if (circular) {
    		mScroller.fling(0, getScrollY(), 0, -velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    	} else {
    		mScroller.fling(0, getScrollY(), 0, -velocityY, 0, 0, minScroll, maxScroll);
    	}
    }
}