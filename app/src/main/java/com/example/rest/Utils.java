package com.example.rest;

import android.app.Activity;

public class Utils {

    private static Utils INSTANCE;
    private Activity mActivity;

    public static Utils getInstance() {
        return INSTANCE;
    }

    private Utils(Activity activity) {
        mActivity = activity;
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
}
