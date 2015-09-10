package me.hqythu.ihs.message.backend;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;

import me.hqythu.ihs.message.MessageApplication;
import me.hqythu.ihs.message.data.MessageSession;

/**
 * Created by hqythu on 9/11/2015.
 */
public class AlarmManager {
    private static HashMap<String, PendingIntent> pendingAlarmIntent = new HashMap<>();

    public static void addAlarm(MessageSession session) {
        Context context = MessageApplication.getInstance();
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.CONTACT_MID, session.contactMid);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        android.app.AlarmManager alarmMgr = (android.app.AlarmManager)
            context.getSystemService(context.ALARM_SERVICE);
        alarmMgr.set(android.app.AlarmManager.RTC_WAKEUP, session.snoozeDate.getTime(), sender);
        pendingAlarmIntent.put(session.contactMid, sender);
    }

    public static void removeAlarm(MessageSession session) {
        removeAlarm(session.contactMid);
    }

    public static void removeAlarm(String contactMid) {
        Context context = MessageApplication.getInstance();
        android.app.AlarmManager alarmMgr = (android.app.AlarmManager)
            context.getSystemService(context.ALARM_SERVICE);
        if (pendingAlarmIntent.containsKey(contactMid)) {
            alarmMgr.cancel(pendingAlarmIntent.get(contactMid));
            pendingAlarmIntent.remove(contactMid);
        }
    }
}
