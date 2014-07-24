package com.example.rest.elements;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.example.rest.R;
import com.example.rest.Utils;

public class RoundButton extends RelativeLayout {

    private final static int sSizeButton = 7;

    private ToggleButton buttons[] = new ToggleButton[sSizeButton];

    private int centerX;
    private int centerY;


    public RoundButton(Context context) {
        super(context);
        initUi(context);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUi(context);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUi(context);
    }

    private void initUi(Context context) {

        Display display = Utils.getInstance().getActivity().getWindowManager().getDefaultDisplay();

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

        RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        setLayoutParams(mainParams);

        centerY = height / 2;
        centerX = width / 2;

        final int paramsButton = context.getResources().getDisplayMetrics().widthPixels / 3;
        setButtonsAround(paramsButton);
    }

    private void setButtonsAround(int paramsButton) {
        String week[] = getContext().getResources().getStringArray(R.array.week);
        //noinspection ConstantConditions
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.font));
        for (int i = 0; i < sSizeButton; i++) {
            LayoutParams buttonParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            float degrees = (float) (-Math.PI * 3 / 5 + 2 * Math.PI / sSizeButton * i) * 11 / 14;
            float x = (float) (centerX + (centerY - paramsButton) * Math.cos(degrees));
            float y = (float) (centerY + (centerY - paramsButton * 1.2) * Math.sin(degrees));
            buttonParams.topMargin = (int) ( y - paramsButton / 3.5);
            buttonParams.leftMargin = (int) x - paramsButton / 3;
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
}
