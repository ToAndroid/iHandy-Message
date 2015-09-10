package me.hqythu.ihs.message.backend;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import me.hqythu.ihs.message.R;
import me.hqythu.ihs.message.ui.MainActivity;

/**
 * Created by hqythu on 9/11/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final int ALARM = 2;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("有新消息")
            .setContentText("延后处理")
            .setAutoCancel(true);
        Intent resultIntent = new Intent(context, MainActivity.class);
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

        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }
}
