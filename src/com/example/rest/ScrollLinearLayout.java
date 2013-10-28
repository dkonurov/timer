package com.example.rest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    private Scroller mScroller;

    private EditText pickerForScroll[];

    private Button plus;

    private AnimationDrawable frameAnimationUp, frameAnimationDown;

    private Button minus;

    private CountDownTimer timerStopScroll;

    private EditText picker;

    private OnScrollListener listener;

    private VelocityTracker mVelocityTracker;

    private int time;
    private int maxTime;

    private int indentScroll;

    private int mLastY, mScrollY, y, diffY, saveScroll, saveScrollPos = 0;

    private int setIdPlus = plusId + 1, setIdMinus = minusId -1
            , setIntPlus = 0, setIntMinus = 0;

    private int setPlusPos = HeightView/2, setMinusPos = HeightView/2;

    private int initialVelocity = 0;

    private boolean checkerDiffY = false, checker = true, once = true, checkEndScroll = false;

	private int mMinimumVelocity, touchSlop;




    public ScrollLinearLayout(final Context Context, Integer Time, Integer MaxTime) {
        super(Context);
        mScroller = new Scroller(getContext());
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(WidthView+indent, 3*HeightView));

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        touchSlop = configuration.getScaledTouchSlop();

        time = Time;
        maxTime = MaxTime;

        shapeTimer = getContext().getResources().getDrawable(R.drawable.text_for_timer);
        /* freeze now!
        plus = new Button(Context);
        plus.setLayoutParams(layoutParams);
        plusShape = getContext().getResources().getDrawable(R.drawable.button_up_selector);
        plus.setGravity(Gravity.CENTER);
        plus.setBackgroundDrawable(plusShape);
        plus.setId(plusId);
        plus.setOnClickListener(this);
        plus.setOnTouchListener(this);
        plus.setTextSize(TextSize);
        plus.setTextColor(Color.BLACK);

        plus.setBackgroundResource(R.anim.change_drawable_up);
		*/
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
        /*freeze now
        minus = new Button(Context);
        minus.setLayoutParams(layoutParams);
        minusShape = getContext().getResources().getDrawable(R.drawable.button_down_selector);
        minus.setGravity(Gravity.CENTER);
        minus.setBackgroundDrawable(minusShape);
        minus.setId(minusId);
        minus.setOnClickListener(this);
        minus.setOnTouchListener(this);
        minus.setTextSize(TextSize);
        minus.setTextColor(Color.BLACK);

        minus.setBackgroundResource(R.anim.change_drawable_down);
		*/
        
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
        scrollBy(0,HeightView);
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
                setIntPlus = 0;
                setIntMinus = 0;
                break;
            case minusId:
                time = minusTime(time);
                picker.setText(time+"");
                setIntPlus = 0;
                setIntMinus = 0;
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                y = (int) event.getRawY();
                if (setIntPlus == 0 && setIntMinus == 0) {
                    setIntPlus = time;
                    setIntMinus = time;
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
                    Log.v("diffY", diffY+"");
                    
                    scrollBy(0,mScrollY);
                    saveScroll += mScrollY;
                    Log.v("saveScroll", saveScroll+"");
                }

                return true;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(100);
                initialVelocity = (int) velocityTracker.getYVelocity();
                Log.v("initialVelocity", initialVelocity+"");
                mScroller.fling(0, diffY, 0, -initialVelocity, 0, 0, 0, Integer.MAX_VALUE);
                Log.v("HeightView", HeightView+"");
                if(checkerDiffY) {
                	
                    mLastY = 0;
                    /*
                    correctedScroll();
                    if (checkEndScroll) {
                        timerStopScroll.cancel();
                    }
                    timerStopScroll = new CountDownTimer(5000,10) {

                        @Override
                        public void onFinish() {
                            // TODO Auto-generated method stub
                            endScroll();
                            setPlusPos = HeightView/2;
                            setMinusPos = HeightView/2;
                            checkerDiffY = false;
                            once = true;
                            saveScroll = 0;
                            checkEndScroll = false;
                        }

                        @Override
                        public void onTick(long arg0) {
                            // TODO Auto-generated method stub
                            if (checkerDiffY) {
                                cancel();
                            }
                        }

                    };
                    checkEndScroll = true;
                    timerStopScroll.start();
                    */
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

    private void setText(int set, EditText timer, boolean check) {
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
        if (check) {
            timer.setText(time+"");
        }
    }

    private void CleanAll(View first, View second, View third) {
        scrollTo(0,0);
        removeAllViews();
        addView(first,plusId);
        addView(second,pickerId);
        addView(third,minusId);

    }

    private void endScroll () {
        double scrollY = (double) saveScroll/HeightView;
        int seter = returnTrueTime(scrollY);
        saveScrollPos = 0;
        setIntPlus = 0;
        setIntMinus = 0;
        int pos, posPlus;
        pos = minusTime(time);
        posPlus = plusTime(time);
        CleanAll(pickerForScroll[posPlus], pickerForScroll[time], pickerForScroll[pos]);
        checker = true;
    }

    private void correctedScroll() {
        int indent = HeightView - 1;
        int absSaveScroll = Math.abs(saveScroll);
        int finder = (int) absSaveScroll%(indent);
        int absFinder = Math.abs(finder);
        if (absFinder<indent/2) {
            if (saveScroll > 0) {
                finder *=-1;
            }
        } else {
            int seter = indent - absFinder;
            if (saveScroll < 0) {
                finder = seter*-1;
            } else {
                finder = seter;
            }
        }
        /*
        saveScroll += finder;
        scrollBy(0,finder);

        double ScrollY = (double) saveScroll/HeightView;
        int seter = returnTrueTime(ScrollY);
        setText(seter, picker, false);

        /*int plusTime = plusTime(time);
        int minusTime = minusTime(time);
        CleanAll(pickerForScroll[plusTime], pickerForScroll[time],pickerForScroll[minusTime]);

        setIntPlus = plusTime;
        setIntMinus = minusTime;
        setPlusPos = HeightView/4;
        setMinusPos = HeightView/4;
		*/
        saveScroll = 0;
        checker = false;

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
        return scrollEditText;
    }
    
    @Override
	public void computeScroll() {
    	if (mScroller.computeScrollOffset()) {
    		mScrollY = mScroller.getCurrY();
    		Log.v("mScrollY", mScrollY+"");
    		postInvalidate();
    	}
    }
}