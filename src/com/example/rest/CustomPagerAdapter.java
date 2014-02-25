package com.example.rest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * Created by dmitry on 16.02.14.
 */
public class CustomPagerAdapter extends PagerAdapter {

    private Context context;
    private TimerLayout timerLayout;
    private View alarmView;
    private Activity activity;

    public CustomPagerAdapter() {
        this.context = Utils.activity.getApplicationContext();
        this.activity = Utils.activity;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        timerLayout = new TimerLayout(activity);
        alarmView = inflater.inflate(R.layout.alarm_view, null);
        FrameLayout alarmLayout = (FrameLayout) alarmView.findViewById(R.id.alarm_layout);

        EditText hour = (EditText) alarmView.findViewById(R.id.hour);
        EditText minute = (EditText) alarmView.findViewById(R.id.minute);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.font));
        hour.setTypeface(typeface);
        minute.setTypeface(typeface);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            hour.setBackgroundDrawable(null);
            minute.setBackgroundDrawable(null);
        } else {
            hour.setBackground(null);
            minute.setBackground(null);
        }

        RoundButton buttons = new RoundButton(context);

        alarmLayout.addView(buttons);

        switch (position) {
            case 1:
                collection.addView(timerLayout, 0);
                return timerLayout;
            case  0:
                collection.addView(alarmView, 0);
                return alarmView;
        }
        collection.addView(alarmView, 0);
        return alarmView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

}
