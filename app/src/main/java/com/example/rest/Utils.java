package com.example.rest;

import android.app.Activity;

import com.example.rest.model.Alarm;

import java.util.List;

public class Utils {

    private static Utils INSTANCE;
    private Activity mActivity;
    private PreferenceManager mPreferenceManager;
    private List<Alarm> mAlarmList;

    public static Utils getInstance() {
        return INSTANCE;
    }


    private Utils(Activity activity) {
        mActivity = activity;
        mPreferenceManager = new PreferenceManager(activity);
    }

    public static void initInstance(Activity activity) {
        INSTANCE = new Utils(activity);
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    public void setPreferenceManager(PreferenceManager mPreferenceManager) {
        this.mPreferenceManager = mPreferenceManager;
    }

    public List<Alarm> getAlarmList() {
        return mAlarmList;
    }

    public void setAlarmList(List<Alarm> mAlarmList) {
        this.mAlarmList = mAlarmList;
    }
}
