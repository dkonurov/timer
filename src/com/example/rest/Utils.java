package com.example.rest;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Utils for help set height or width, and get context, activity from all Class
 */
public class Utils {

    public static Context context;
    public static Activity activity;
    public static TimerSharedPreferances timerSharedPreferances;
    public static DisplayMetrics metrics = new DisplayMetrics();

    public Utils(Activity activity) {
        Utils.context = activity.getApplicationContext();
        Utils.activity = activity;
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    /**
     * get Dimenision from demins.xml
     * @param id
     * @return int dimension
     */
    public static int getDimension(int id) {
        Resources r = context.getResources();
        return (int) r.getDimension(id);
    }

    /**
     * Translates from dp to px
     *
     * @param dp
     * @return float px
     */
    public static float dpToPx(int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                r.getDisplayMetrics());
        return px;
    }
}