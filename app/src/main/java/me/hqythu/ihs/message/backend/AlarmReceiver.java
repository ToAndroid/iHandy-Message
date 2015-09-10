package me.hqythu.ihs.message.backend;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import de.greenrobot.event.EventBus;
import me.hqythu.ihs.message.R;
import me.hqythu.ihs.message.data.MessageSession;
import me.hqythu.ihs.message.db.SessionDBManager;
import me.hqythu.ihs.message.event.SessionStatusChangeEvent;
import me.hqythu.ihs.message.ui.ChatActivity;
import me.hqythu.ihs.message.ui.MainActivity;

/**
 * Created by hqythu on 9/11/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final int ALARM = 2;
    public static final String CONTACT_MID = "ContactMid";
    @Override
    public void onReceive(Context context, Intent intent) {
        String contactMid = intent.getStringExtra(CONTACT_MID);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("有新消息")
            .setContentText("延后处理")
            .setAutoCancel(true);
        Intent resultIntent = new Intent(context, ChatActivity.class);
        resultIntent.putExtra(ChatActivity.CHAT_MID, contactMid);
        PendingIntent resultPendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMgr =
            (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(ALARM, mBuilder.build());

        MessageSession session = new MessageSession(SessionDBManager.querySession(contactMid));
        AlarmManager.removeAlarm(session);
        session.snoozeDate = null;
        SessionDBManager.setSnoozeDate(session.contactMid, session.snoozeDate);
        EventBus.getDefault().post(new SessionStatusChangeEvent(session, session.getType()));

        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }
}
