package com.example.rest;


import android.content.Context;
import android.content.SharedPreferences;

class TimerSharedPreferances {

    private final static String sAlarmWeek = "ALARM_WEEK";

    public static void addAlarmWeekPreferances(boolean[] weekBoolean, int id) {
        SharedPreferences sPref = Utils.activity.getPreferences(Context.MODE_PRIVATE);
        String savedText = null;
        savedText = sPref.getString(sAlarmWeek, "");
        SharedPreferences.Editor ed = sPref.edit();
        savedText += String.valueOf(id);
        for (int i = 0, length = weekBoolean.length; i < length; i++) {
            if (weekBoolean[i]) {
                savedText += "1";
            } else {
                savedText += "0";
            }
        }
        savedText += "/";
        ed.putString(sAlarmWeek, savedText);
        ed.commit();
    }

    public static  void replaceAlaramWeekPreferances(boolean[] weekBoolean, int id) {
        SharedPreferences sPref = Utils.activity.getPreferences(Context.MODE_PRIVATE);
        String savedText = null;
        savedText = sPref.getString(sAlarmWeek, "");
        SharedPreferences.Editor ed = sPref.edit();
        int position = id * AlarmModel.sLengthWeek;
        int positionLast = (id+2) * AlarmModel.sLengthWeek;
        String processSaveTextFirst = savedText.substring(0, position);
        String processSaveTextLast;

        if (positionLast < savedText.length()) {
            processSaveTextLast = savedText.substring(position, savedText.length());
        } else {
            processSaveTextLast = null;
        }
        savedText = processSaveTextFirst;
        savedText += String.valueOf(id);
        for (int i = 0, length = weekBoolean.length; i < length; i++) {
            if (weekBoolean[i]) {
                savedText += "1";
            } else {
                savedText += "0";
            }
        }
        savedText += "/";
        savedText += processSaveTextLast;
        ed.putString(sAlarmWeek, savedText);
        ed.commit();
    }

    public static void deleteAlarmWeekPreferances(int id) {
        SharedPreferences sPref = Utils.activity.getPreferences(Context.MODE_PRIVATE);
        String savedText = null;
        savedText = sPref.getString(sAlarmWeek, "");
        SharedPreferences.Editor ed = sPref.edit();
        String[] process = savedText.split("/");
        savedText = null;
        for (int i = 0, length = process.length; i < length; i++) {
            if (i != id) {
                savedText += process[i];
            }
        }
        ed.putString(sAlarmWeek, savedText);
        ed.commit();
    }

    public static String getAlarmWeekPreferances(int id) {
        SharedPreferences sPref = Utils.activity.getPreferences(Context.MODE_PRIVATE);
        String savedText = null;
        savedText = sPref.getString(sAlarmWeek, "");
        String process[] = savedText.split("/");
        return process[id];
    }
}
