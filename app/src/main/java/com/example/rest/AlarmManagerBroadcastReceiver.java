package com.example.rest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;

import com.example.rest.model.Alarm;

import java.util.Calendar;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    /* check for sound and vibration */
    private boolean sound;
    private boolean vibration;

    /**
     * set sound and vibration check
     *
     * @param _sound
     * @param _vibration
     */
    public void setChecker(boolean _sound, boolean _vibration) {
        sound = _sound;
        vibration = _vibration;
    }


    /**
     * Set pereodic alarm
     *
     * @param context
     * @param startHours
     * @param startMinute
     * @param endHours
     * @param endMinute
     * @param periodicHours
     * @param periodicMinute
     */
    public void SetAlarm(Context context, int startHours, int startMinute, int endHours, int endMinute, int periodicHours, int periodicMinute, boolean setDate) {
        AlarmManager startAm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager endAm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, TimerService.class);

        intent.putExtra("sound", sound);
        intent.putExtra("vibration", vibration);

        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        calendarStart.set(Calendar.MINUTE, startMinute);
        if (setDate) {
            calendarStart.set(Calendar.HOUR_OF_DAY, startHours);
        } else {
            calendarStart.set(Calendar.HOUR, startHours);
        }

        calendarEnd.set(Calendar.MINUTE, endMinute);
        if (setDate) {
            calendarEnd.set(Calendar.HOUR_OF_DAY, endHours);
        } else {
            calendarEnd.set(Calendar.HOUR, endHours);
        }


        calendarStart.set(Calendar.SECOND, 0);
        calendarEnd.set(Calendar.SECOND, 0);

        PendingIntent piStart = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piEnd = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        int differenceHours = endHours - startHours;
        if (startHours > endHours) {
            differenceHours = endHours + 24 - startHours;
        }

        while (calendarStart.getTimeInMillis() < now.getTimeInMillis()) {
            startHours += periodicHours;
            startMinute += periodicMinute;
            calendarStart.set(Calendar.MINUTE, startMinute);
            if (setDate) {
                calendarStart.set(Calendar.HOUR_OF_DAY, startHours);
            } else {
                calendarStart.set(Calendar.HOUR, startHours);
            }
        }

        while (calendarEnd.getTimeInMillis() < now.getTimeInMillis()) {
            endHours += periodicHours;
            endMinute += periodicMinute;
            calendarEnd.set(Calendar.MINUTE, endMinute);
            if (setDate) {
                calendarEnd.set(Calendar.HOUR_OF_DAY, endHours);
            } else {
                calendarEnd.set(Calendar.HOUR, endHours);
            }

        }
        int difference = differenceHours * 60 + endMinute - startMinute;
        int periodic = periodicHours * 60 + periodicMinute + difference;
        startAm.setRepeating(AlarmManager.RTC_WAKEUP, calendarStart.getTimeInMillis(), 1000 * 60 * periodic, piStart);
        endAm.setRepeating(AlarmManager.RTC_WAKEUP, calendarEnd.getTimeInMillis(), 1000 * 60 * periodic, piEnd);
    }

    /**
     * set once alarm
     *
     * @param context
     * @param alarm
     */
    public void setAlarm(Context context, Alarm alarm) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, TimerService.class);

        intent.putExtra("sound", sound);
        intent.putExtra("vibration", vibration);

        boolean once = false;

        Boolean[] weekBoolean = alarm.getWeekBoolean();
        for (int i = 0; i < Alarm.sSizeButton; i++) {
            if (weekBoolean[i]) {
                if (!once) {
                    once = true;
                }
                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.MINUTE, alarm.getMinute());
                calendar.set(Calendar.DAY_OF_WEEK, i);
                if (DateFormat.is24HourFormat(context)) {
                    calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
                } else {
                    calendar.set(Calendar.HOUR, alarm.getHour());
                }

                calendar.set(Calendar.SECOND, 0);

                PendingIntent pendingIntent = PendingIntent
                        .getService(context, i + Utils.getInstance().
                                                                getPreferenceManager().getAlarmCount(),
                                intent, PendingIntent.FLAG_UPDATE_CURRENT
                        );

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            }
        }
        if (once) {
            Utils.getInstance().getPreferenceManager().addAlarmCount();
        }

    }

    public void SetAlarm(Context context, int startHours, int startMinute, int endHours, int endMinute, int periodicHours, int periodicMinute) {
        SetAlarm(context, startHours, startMinute, endHours, endMinute, periodicHours, periodicMinute, true);
    }


    /**
     * Cancel seted alarm
     *
     * @param context
     */
    public void CancelAlarm(Context context, Alarm alarm) {
        Intent intent = new Intent(context, TimerService.class);
        Boolean[] weekBoolean = alarm.getWeekBoolean();
        for (int i = 0; i <= Alarm.sSizeButton; i++) {
            if (weekBoolean[i]) {
                PendingIntent pendingIntent = PendingIntent.getService(context, alarm.getId() + i, intent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);

            }
        }

    }

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub

    }
}