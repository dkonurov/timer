package com.example.rest.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.rest.elements.AlarmLayout;
import com.example.rest.elements.TimerLayout;

public class CustomPagerAdapter extends PagerAdapter {

    private TimerLayout timerLayout;
    private AlarmLayout alarmLayout;

    public CustomPagerAdapter(AlarmLayout alarmLayout, TimerLayout timerLayout) {
        this.alarmLayout = alarmLayout;
        this.timerLayout = timerLayout;
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

        switch (position) {
            case 1:
                collection.addView(timerLayout, 0);
                return timerLayout;
            case  0:
                collection.addView(alarmLayout, 0);
                return alarmLayout;
        }
        collection.addView(alarmLayout, 0);
        return alarmLayout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

}
