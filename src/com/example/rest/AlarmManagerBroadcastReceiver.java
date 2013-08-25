package com.example.rest;
import java.util.Calendar;
 
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
 
 
 public void SetAlarm(Context context, int start, int end, int periodic)
    {
        AlarmManager startAm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        AlarmManager endAm =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(context, TimerService.class);
        
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        
        int hoursEnd = 0;
        int hoursStart = calendarStart.get(Calendar.HOUR_OF_DAY);
        
        if(calendarStart.get(Calendar.MINUTE) >= start) {
        	hoursStart +=1;
        }
        if(start < end) {
        	hoursEnd = hoursStart;
        } else {
        	hoursEnd = hoursStart+1;
        }
        calendarStart.set(Calendar.MINUTE, start);
        calendarStart.set(Calendar.HOUR_OF_DAY, hoursStart);
        	
        calendarEnd.set(Calendar.MINUTE, end);
        calendarEnd.set(Calendar.HOUR_OF_DAY, hoursEnd);
        	
        calendarStart.set(Calendar.SECOND, 0);
        calendarEnd.set(Calendar.SECOND, 0);
        
        PendingIntent piStart = PendingIntent.getService(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piEnd = PendingIntent.getService(context, 1, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        
        startAm.setRepeating(AlarmManager.RTC_WAKEUP, calendarStart.getTimeInMillis(), 1000 * 60 * periodic , piStart);
        endAm.setRepeating(AlarmManager.RTC_WAKEUP, calendarEnd.getTimeInMillis(), 1000 * 60 *  periodic , piEnd);
   }
 
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