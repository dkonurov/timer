package com.example.rest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.rest.model.Alarm;

public class PreferenceManager {

    private final static String ALARM_COUNT = "ALARM_COUNT";
    private final static int DEFAULT_COUNT = 0;
    private SharedPreferences mSharedPreference;

    public PreferenceManager(Activity activity) {
        mSharedPreference = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void addAlarmCount() {
        int currentCount = getAlarmCount();
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putInt(ALARM_COUNT, currentCount + Alarm.sSizeButton);
        editor.apply();
    }

    public int getAlarmCount() {
        return mSharedPreference.getInt(ALARM_COUNT, DEFAULT_COUNT);
    }

    public void removeAlarmCount() {
        int currentCount = getAlarmCount();
        if (currentCount == 0) {
            return;
        } else {
            SharedPreferences.Editor editor = mSharedPreference.edit();
            editor.putInt(ALARM_COUNT, currentCount - Alarm.sSizeButton);
            editor.apply();
        }
    }
}
