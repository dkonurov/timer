package com.example.rest.elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.example.rest.R;
import com.example.rest.model.Alarm;

@SuppressLint("ViewConstructor")
public class RoundButton extends RelativeLayout {

    private final View mCenterView;

    private ToggleButton buttons[] = new ToggleButton[Alarm.sSizeButton];

    private int centerX;
    private int centerY;


    public RoundButton(Context context, View centerView) {
        super(context);
        mCenterView = centerView;
        initUi(context);
    }

    private void initUi(Context context) {

        for (int i = 0; i < Alarm.sSizeButton; i++) {
            buttons[i] = new ToggleButton(context);
        }

        setButtonBackground(context);

        RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        setLayoutParams(mainParams);

        if (mCenterView != null) {
            mCenterView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            centerY = (int) (getResources().getDimension(R.dimen.top_margin_edit_text) + (mCenterView.getMeasuredHeight() / 3));
            centerX = (int) (getResources().getDimension(R.dimen.left_margin_edit_text) + (mCenterView.getMeasuredWidth() / 2.4));

            final int paramsButton = context.getResources().getDisplayMetrics().widthPixels / 6;
            setButtonsAround(paramsButton);
        }
    }

    private void setButtonsAround(int paramsButton) {
        String week[] = getContext().getResources().getStringArray(R.array.week);
        //noinspection ConstantConditions
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.font));
        for (int i = 0; i < Alarm.sSizeButton; i++) {
            LayoutParams buttonParams = new LayoutParams(paramsButton, paramsButton);
            float degrees = (float) (-Math.PI * 3 / 5 + 2 * Math.PI / Alarm.sSizeButton * i) * 9 / 12;
            float x = (float) (centerX + ((paramsButton + paramsButton) * Math.cos(degrees)));
            float y = (float) (centerY + ((paramsButton + paramsButton / 1.7) * Math.sin(degrees)));
            buttonParams.topMargin = (int) y;
            buttonParams.leftMargin = (int) x;
            buttons[i].setGravity(Gravity.CENTER);
            buttons[i].setTextOff(week[i]);
            buttons[i].setTextOn(week[i]);
            buttons[i].setText(week[i]);
            buttons[i].setLayoutParams(buttonParams);
            buttons[i].setTypeface(typeface);
            buttons[i].setTextColor(Color.BLACK);
            addView(buttons[i]);
        }
    }

    private void setButtonBackground(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            buttons[0].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.monday_selector));
            buttons[1].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tuesday_selector));
            buttons[2].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.wednesday_selector));
            buttons[3].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.thursday_selector));
            buttons[4].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.friday_selector));
            buttons[5].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.saturday_selector));
            buttons[6].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sunday_selector));
        } else {
            buttons[0].setBackground(context.getResources().getDrawable(R.drawable.monday_selector));
            buttons[1].setBackground(context.getResources().getDrawable(R.drawable.tuesday_selector));
            buttons[2].setBackground(context.getResources().getDrawable(R.drawable.wednesday_selector));
            buttons[3].setBackground(context.getResources().getDrawable(R.drawable.thursday_selector));
            buttons[4].setBackground(context.getResources().getDrawable(R.drawable.friday_selector));
            buttons[5].setBackground(context.getResources().getDrawable(R.drawable.saturday_selector));
            buttons[6].setBackground(context.getResources().getDrawable(R.drawable.sunday_selector));
        }
    }

    public Boolean[] getWeekBoolean() {
        Boolean[] weekBoolean = new Boolean[Alarm.sSizeButton];
        for (int i = 0; i < Alarm.sSizeButton; i++) {
            weekBoolean[i] = buttons[i].isChecked();
        }
        return weekBoolean;
    }
}
