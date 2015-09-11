package me.hqythu.ihs.message.backend;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.ihs.demo.message.FriendManager;

import de.greenrobot.event.EventBus;
import me.hqythu.ihs.message.R;
import me.hqythu.ihs.message.data.MessageSession;
import me.hqythu.ihs.message.db.SessionDBManager;
import me.hqythu.ihs.message.event.SessionStatusChangeEvent;
import me.hqythu.ihs.message.ui.ChatActivity;

/**
 * Created by hqythu on 9/11/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final int ALARM = 2;
    public static final String CONTACT_MID = "ContactMid";
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        String contactMid = intent.getStringExtra(CONTACT_MID);
        String name = FriendManager.getInstance().getFriend(contactMid).getName();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("有新通知")
            .setContentText(name)
            .setSound(Uri.parse("android.resource://me.hqythu.ihs.message/ras/message_ringtone_received"))
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
        SessionDBManager.setSnoozeDate(session.contactMid, null);

        EventBus.getDefault().post(new SessionStatusChangeEvent(session, session.getType()));

        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        wl.release();
    }
}
