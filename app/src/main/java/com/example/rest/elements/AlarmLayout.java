package com.example.rest.elements;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.rest.R;

public class AlarmLayout extends FrameLayout {

    public AlarmLayout(Activity activity) {
        super(activity);

        View.inflate(activity, R.layout.alarm_view, this);
        initUi(activity);
    }


    @SuppressWarnings("deprecation")
    private void initUi(Activity activity) {
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

        RoundButton buttons = new RoundButton(activity);

        addView(buttons);
    }
}
