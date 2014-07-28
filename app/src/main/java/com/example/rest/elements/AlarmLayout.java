package com.example.rest.elements;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.rest.R;

public class AlarmLayout extends FrameLayout {

    public AlarmLayout(Context context) {
        super(context);
        View.inflate(getContext(), R.layout.alarm_view, this);
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

        LinearLayout centerViewForButton = (LinearLayout) findViewById(R.id.edit_text_layout);

        RoundButton buttons = new RoundButton(getContext(), centerViewForButton);

        addView(buttons);
    }
}
