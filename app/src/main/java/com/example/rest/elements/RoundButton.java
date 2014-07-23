package com.example.rest.elements;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.example.rest.R;

public class RoundButton extends RelativeLayout {

    private final static int sSizeButton = 7;

    private ToggleButton buttons[] = new ToggleButton[sSizeButton];

    private int centerX;
    private int centerY;

    public RoundButton(Activity activity) {
        super(activity);
        initUi(activity);
    }

    private void initUi(Activity activity) {
        Context context = activity.getApplicationContext();

        Display display = activity.getWindowManager().getDefaultDisplay();

        for (int i = 0; i < sSizeButton; i++) {
            buttons[i] = new ToggleButton(context);
        }

        int height;
        int width;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            //noinspection deprecation
            height = display.getHeight();
            //noinspection deprecation
            width = display.getWidth();
            buttons[0].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.monday_selector));
            buttons[1].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tuesday_selector));
            buttons[2].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.wednesday_selector));
            buttons[3].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.thursday_selector));
            buttons[4].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.friday_selector));
            buttons[5].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.saturday_selector));
            buttons[6].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sunday_selector));
        } else {
            Point point = new Point();
            display.getSize(point);
            height = point.y * 2 / 3;
            width = point.x;

            buttons[0].setBackground(context.getResources().getDrawable(R.drawable.monday_selector));
            buttons[1].setBackground(context.getResources().getDrawable(R.drawable.tuesday_selector));
            buttons[2].setBackground(context.getResources().getDrawable(R.drawable.wednesday_selector));
            buttons[3].setBackground(context.getResources().getDrawable(R.drawable.thursday_selector));
            buttons[4].setBackground(context.getResources().getDrawable(R.drawable.friday_selector));
            buttons[5].setBackground(context.getResources().getDrawable(R.drawable.saturday_selector));
            buttons[6].setBackground(context.getResources().getDrawable(R.drawable.sunday_selector));
        }

        FrameLayout.LayoutParams mainParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        setLayoutParams(mainParams);

        centerY = height / 2;
        centerX = width / 2;

        Resources r = context.getResources();
        final int paramsButton;
        final int textSize;
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenLayout == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            paramsButton = (int) context.getResources().getDimension(R.dimen.button_params_small);
            textSize = (int) context.getResources().getDimension(R.dimen.text_size_button_small);
            correctSmallButtons(r, paramsButton, textSize);
        } else if (screenLayout == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            paramsButton = (int) context.getResources().getDimension(R.dimen.button_params_normal);
            textSize = (int) context.getResources().getDimension(R.dimen.text_size_button_normal);
            correctNormalButtons(r, paramsButton, textSize);
        } else {
            paramsButton = (int) context.getResources().getDimension(R.dimen.button_params_large);
            textSize = (int) context.getResources().getDimension(R.dimen.text_size_button_large);
            correctNormalButtons(r, paramsButton, textSize);
        }
    }

    private void correctSmallButtons(Resources r, int paramsButton, int textSize) {
        String week[] = r.getStringArray(R.array.week);
        //noinspection ConstantConditions
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.font));
        for (int i = 0; i < sSizeButton; i++) {
            LayoutParams buttonParams = new LayoutParams(paramsButton, paramsButton);
            float degrees = (float) (-Math.PI * 3 / 5 + 2 * Math.PI / sSizeButton * i) * 11 / 14;
            float x = (float) (centerX + (centerY - paramsButton * 0.3) * Math.cos(degrees));
            float y = (float) (centerY + (centerY - paramsButton * 0.65) * Math.sin(degrees));
            buttonParams.topMargin = (int) y;
            buttonParams.leftMargin = (int) x - paramsButton / 3;
            if (i == 4) {
                buttons[i].setGravity(Gravity.RIGHT | Gravity.BOTTOM);
            } else if (i == 5 || i == 6 || i == 3) {
                buttons[i].setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            }

            buttons[i].setTextOff(week[i]);
            buttons[i].setTextOn(week[i]);
            buttons[i].setText(week[i]);
            buttons[i].setTextSize(textSize);
            buttons[i].setLayoutParams(buttonParams);
            buttons[i].setTypeface(typeface);
            buttons[i].setTextColor(Color.BLACK);
            addView(buttons[i]);
        }
    }

    private void correctNormalButtons(Resources r, int paramsButton, int textSize) {
        String week[] = r.getStringArray(R.array.week);
        //noinspection ConstantConditions
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.font));
        for (int i = 0; i < sSizeButton; i++) {
            LayoutParams buttonParams = new LayoutParams(paramsButton, paramsButton);
            float degrees = (float) (-Math.PI * 3 / 5 + 2 * Math.PI / sSizeButton * i) * 11 / 14;
            float x = (float) (centerX + (centerY - paramsButton) * Math.cos(degrees));
            float y = (float) (centerY + (centerY - paramsButton * 1.2) * Math.sin(degrees));
            buttonParams.topMargin = (int) y - paramsButton / 4;
            buttonParams.leftMargin = (int) x - paramsButton / 3;
            if (i == 4) {
                buttons[i].setGravity(Gravity.RIGHT | Gravity.BOTTOM);
            } else if (i == 5 || i == 6 || i == 3) {
                buttons[i].setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            }

            buttons[i].setTextOff(week[i]);
            buttons[i].setTextOn(week[i]);
            buttons[i].setText(week[i]);
            buttons[i].setTextSize(textSize);
            buttons[i].setLayoutParams(buttonParams);
            buttons[i].setTypeface(typeface);
            buttons[i].setTextColor(Color.BLACK);
            addView(buttons[i]);
        }
    }
}
