package com.example.rest.elements;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.rest.AlarmModel;
import com.example.rest.R;
import com.example.rest.Utils;
import com.example.rest.adapters.AlarmListAdapter;

import java.util.ArrayList;
import java.util.Random;

public class AlarmLayout extends FrameLayout implements View.OnTouchListener {

    private static final int rotationInvisible = 90;

    private FrameLayout mAlarmLayout;
    private FrameLayout mAlarmList;

    /*
     * if true that's visible mAlarmLayout, else visible is mAlarmList
     */
    private boolean isVisibleView;

    private float mStartY = 0;
    private float mMoveY = 0;
    private float mDiffMoveY = 0;


    private int touchSlap;

    private VelocityTracker velocityTracker;
    private float mRotationLayout = 0;
    private float mRotationList = 0;

    public AlarmLayout(Context context, boolean isVisibleView) {
        super(context);
        this.isVisibleView = isVisibleView;
        mAlarmLayout = new FrameLayout(context);
        mAlarmList = new FrameLayout(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        touchSlap = configuration.getScaledTouchSlop();
        velocityTracker = VelocityTracker.obtain();

        View.inflate(context, R.layout.alarm_view, mAlarmLayout);
        View.inflate(context, R.layout.alarm_list, mAlarmList);
        initUi();
        setOnTouchListener(this);
    }


    @SuppressWarnings("deprecation")
    private void initUi() {
        EditText hour = (EditText) mAlarmLayout.findViewById(R.id.hour);
        EditText minute = (EditText) mAlarmLayout.findViewById(R.id.minute);
        //noinspection ConstantConditions
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
                getContext().getString(R.string.font));
        hour.setTypeface(typeface);
        minute.setTypeface(typeface);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            hour.setBackgroundDrawable(null);
            minute.setBackgroundDrawable(null);
        } else {
            hour.setBackground(null);
            minute.setBackground(null);
        }

        RoundButton buttons = new RoundButton(getContext());

        mAlarmLayout.addView(buttons);
        addView(mAlarmLayout);

        ArrayList<AlarmModel> alarmModelList = new ArrayList<AlarmModel>();
        for (int i = 0; i < 5; i++) {
            boolean[] weekBoolean = new boolean[7];
            Random random = new Random();
            for (int j = 0; j < 7; j++) {
                weekBoolean[j] = random.nextBoolean();
            }
            int[] time = new int[2];
            for (int j = 0; j < 2; j++) {
                time[j] = random.nextInt();
            }
            AlarmModel alarmModel = new AlarmModel(weekBoolean, time);
            int randomInt = Utils.metrics.widthPixels;
            alarmModel.setLeftPadding(random.nextInt(randomInt));
            alarmModelList.add(alarmModel);
        }
        AlarmListAdapter alarmListAdapter = new AlarmListAdapter(alarmModelList);
        ListView alarmList = (ListView) mAlarmList.findViewById(R.id.alarm_list);
        alarmList.setAdapter(alarmListAdapter);



        addView(mAlarmList);
        if (isVisibleView) {
            mAlarmList.setVisibility(View.INVISIBLE);
            mRotationList = rotationInvisible + 1;
        } else {
            mAlarmLayout.setVisibility(View.INVISIBLE);
            mRotationLayout = rotationInvisible + 1;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartY = motionEvent.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mMoveY == 0) {
                    mMoveY = mStartY;
                }
                mDiffMoveY = motionEvent.getRawY() - mMoveY;
                mMoveY = motionEvent.getRawY();
                float diff = Math.abs(mMoveY - mStartY);
                float rotation = mMoveY - mStartY;
                if (diff >= touchSlap && diff > 0) {
                    setRotationView(rotation);
                    return true;
                }
        }
        return false;
    }

    private void setRotationView(float diff) {
        if (isVisibleView) {
            mRotationLayout = -diff * rotationInvisible / (Utils.metrics.heightPixels - mStartY) * 2;
            if (mRotationLayout > 0) {
                mRotationLayout = 0;
            }
            mAlarmLayout.setRotationX(mRotationLayout);
        } else {
            mRotationList = diff * rotationInvisible / (Utils.metrics.heightPixels - mStartY) * 2;
            mAlarmList.setRotationX(mRotationList);
        }
        setVisible();
    }

    private void setVisible() {
        if (mRotationLayout <= -rotationInvisible
                && mAlarmList.getVisibility() != View.VISIBLE && isVisibleView) {
            mAlarmLayout.setVisibility(View.INVISIBLE);
            mAlarmList.setVisibility(View.VISIBLE);
            isVisibleView = false;
        } else if (mRotationList <= rotationInvisible
                && mAlarmLayout.getVisibility() != View.VISIBLE && !isVisibleView) {
            mAlarmList.setVisibility(View.INVISIBLE);
            mAlarmLayout.setVisibility(View.VISIBLE);
            isVisibleView = true;
        }
    }
}
