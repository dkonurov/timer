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

public class AlarmLayout extends FrameLayout {

    public AlarmLayout(Context context) {
        super(context);

        View.inflate(context, R.layout.alarm_view, this);
        initUi();
    }


    @SuppressWarnings("deprecation")
    private void initUi() {
        EditText hour = (EditText) findViewById(R.id.hour);
        EditText minute = (EditText) findViewById(R.id.minute);
        //noinspection ConstantConditions
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
                getContext().getString(R.string.font));
        hour.setTypeface(typeface);
        minute.setTypeface(typeface);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            hour.setBackgroundDrawable(null);
            minute.setBackgroundDrawable(null);
        } else {
            hour.setBackground(null);
            minute.setBackground(null);
        }

        RoundButton buttons = new RoundButton(getContext());

        addView(buttons);
    }
}
