package com.example.rest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
 
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
	
	/* check for sound and vibration */
	private boolean sound;
	private boolean vibration;
	
	/**
	 * set sound and vibration check
	 * @param boolean _sound
	 * @param boolean _vibration
	 */
	public void setChecker (boolean _sound, boolean _vibration) {
		sound = _sound;
		vibration = _vibration;
	}
 

	/**
	 * Set pereodic alarm
	 * @param Context context
	 * @param int startHours
	 * @param int startMinute
	 * @param int endHours
	 * @param int endMinute
	 * @param int periodicHours
	 * @param int periodicMinute
	 */
public void SetAlarm(Context context, int startHours, int startMinute, int endHours, int endMinute, int periodicHours, int periodicMinute, boolean setDate)
    {
        AlarmManager startAm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager endAm =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
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
        
        PendingIntent piStart = PendingIntent.getService(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piEnd = PendingIntent.getService(context, 1, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        
        int differenceHours = endHours - startHours;
        if (startHours>endHours) {
        	differenceHours = endHours+24-startHours;
        }
        
        while (calendarStart.getTimeInMillis()< now.getTimeInMillis()) {
        	startHours += periodicHours;
        	startMinute += periodicMinute;
        	calendarStart.set(Calendar.MINUTE, startMinute);
        	if (setDate) {
            	calendarStart.set(Calendar.HOUR_OF_DAY, startHours);
            } else {
            	calendarStart.set(Calendar.HOUR, startHours);
            }
        }
        
        while (calendarEnd.getTimeInMillis()< now.getTimeInMillis()) {
        	endHours += periodicHours;
        	endMinute += periodicMinute;
        	calendarEnd.set(Calendar.MINUTE, endMinute);
        	if (setDate) {
            	calendarEnd.set(Calendar.HOUR_OF_DAY, endHours);
            } else {
            	calendarEnd.set(Calendar.HOUR, endHours);
            }
        	
        }
        int difference = differenceHours*60 + endMinute - startMinute;
        int periodic = periodicHours*60 + periodicMinute + difference;
        startAm.setRepeating(AlarmManager.RTC_WAKEUP, calendarStart.getTimeInMillis(), 1000 * 60 * periodic , piStart);
        endAm.setRepeating(AlarmManager.RTC_WAKEUP, calendarEnd.getTimeInMillis(), 1000 * 60 *  periodic , piEnd);
   }

	/**
	 * set once alarm
	 * @param Context context
	 * @param int startHours
	 * @param int startMinute
	 * @param int endHours
	 * @param int endMinute
	 */
	public void SetAlarm(Context context, int startHours, int startMinute, int endHours,int endMinute, boolean setDate) {
		
		AlarmManager startAm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager endAm =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
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
        
        PendingIntent piStart = PendingIntent.getService(context, 0, intent,PendingIntent.FLAG_ONE_SHOT);
        
        boolean once = true;
        intent.putExtra("once", once);
        
        PendingIntent piEnd = PendingIntent.getService(context, 1, intent,PendingIntent.FLAG_ONE_SHOT);
        
        while (calendarStart.getTimeInMillis()< now.getTimeInMillis()) {
        	startHours += AlarmManager.INTERVAL_DAY;
        	if (setDate) {
            	calendarStart.set(Calendar.HOUR_OF_DAY, startHours);
            } else {
            	calendarStart.set(Calendar.HOUR, startHours);
            }
        }
        
        while (calendarEnd.getTimeInMillis()< now.getTimeInMillis()) {
        	endHours += AlarmManager.INTERVAL_DAY;
        	if (setDate) {
            	calendarEnd.set(Calendar.HOUR_OF_DAY, endHours);
            } else {
            	calendarEnd.set(Calendar.HOUR, endHours);
            }
        }
        
        startAm.set(AlarmManager.RTC_WAKEUP, calendarStart.getTimeInMillis(), piStart);
        endAm.set(AlarmManager.RTC_WAKEUP, calendarEnd.getTimeInMillis(), piEnd);
        
	}

    public void SetAlarm(Context context, int startHours, int startMinute, int endHours, int endMinute, int periodicHours, int periodicMinute) {
        SetAlarm(context, startHours, startMinute, endHours, endMinute, periodicHours, periodicMinute, true);
    }

    public void SetAlarm(Context context, int startHours, int startMinute, int endHours, int endMinute) {
        SetAlarm(context, startHours, startMinute, endHours, endMinute, true);
    }
 
	
	/**
	 * Cancel seted alarm
	 * @param Context context
	 */
    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, TimerService.class);
        PendingIntent senderStart = PendingIntent.getService(context, 0, intent, 0);
        PendingIntent senderStop = PendingIntent.getService(context, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(senderStart);
        alarmManager.cancel(senderStop);
        
    }
    
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		
	}
}