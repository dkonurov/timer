package com.example.rest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.gesture.GesturePoint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CustomTimePickerDialog extends Dialog implements View.OnClickListener {

    private Context dialogContext;

    private Integer setHours;
    private Integer setMinute;

    private EditText pickerHour;
    private EditText pickerMinute;

    private ScrollLinearLayout hourLayout;
    private ScrollLinearLayout minuteLayout;

    public CustomTimePickerDialog(Context context, EditText mainPickerHour, EditText mainPickerMinute,  Integer startHour, Integer startMinute) {
        super(context);
        dialogContext = context;
        setHours = startHour;
        setMinute = startMinute;
        pickerHour = mainPickerHour;
        pickerMinute = mainPickerMinute;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.fragment_time_picker, null);
        hourLayout = new ScrollLinearLayout(dialogContext,setHours,23, false);
        minuteLayout = new ScrollLinearLayout(dialogContext, setMinute, 59, true);
        LinearLayout TimePicker = (LinearLayout) view.findViewById(R.id.custom_timepicker);
        TimePicker.addView(hourLayout);
        TimePicker.addView(minuteLayout);
        Button setTime = (Button) view.findViewById(R.id.SetTime);
        setTime.setOnClickListener(this);
        setContentView(view);
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        switch (Id) {
            case R.id.SetTime:
                int hour =  hourLayout.getTime();
                int minute = minuteLayout.getTime();
                if (hour<10) {
                    pickerHour.setText("0"+hour);
                } else {
                    pickerHour.setText(hour+"");
                }
                if(minute <10) {
                    pickerMinute.setText("0"+minute);
                } else {
                    pickerMinute.setText(minute+"");
                }
                cancel();
        }
    }


}
