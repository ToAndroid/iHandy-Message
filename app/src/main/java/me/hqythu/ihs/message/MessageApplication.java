package me.hqythu.ihs.message;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.ihs.account.api.account.HSAccountManager;
import com.ihs.app.framework.HSApplication;
import com.ihs.app.framework.HSSessionMgr;
import com.ihs.commons.keepcenter.HSKeepCenter;
import com.ihs.commons.notificationcenter.HSGlobalNotificationCenter;
import com.ihs.commons.notificationcenter.INotificationObserver;
import com.ihs.commons.utils.HSBundle;
import com.ihs.contacts.api.HSPhoneContactMgr;
import com.ihs.demo.message.FriendManager;
import com.ihs.demo.message.SampleFragment;
import com.ihs.message_2012010548.managers.HSMessageChangeListener;
import com.ihs.message_2012010548.managers.HSMessageManager;
import com.ihs.message_2012010548.types.HSBaseMessage;
import com.ihs.message_2012010548.types.HSImageMessage;
import com.ihs.message_2012010548.types.HSOnlineMessage;
import com.ihs.message_2012010548.types.HSTextMessage;
import com.ihs.message_2012010548.utils.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import com.ihs.message_2012010548.friends.api.HSContactFriendsMgr;

import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;
import me.hqythu.ihs.message.data.MessageSession;
import me.hqythu.ihs.message.db.SessionDBManager;
import me.hqythu.ihs.message.event.FriendUpdateEvent;
import me.hqythu.ihs.message.event.MessageAddEvent;
import me.hqythu.ihs.message.event.MessageUpdateEvent;
import me.hqythu.ihs.message.event.SessionUnreadCountChangeEvent;
import me.hqythu.ihs.message.event.SessionUpdateEvent;
import me.hqythu.ihs.message.ui.ChatActivity;
import me.hqythu.ihs.message.ui.MainActivity;

/**
 * Created by hqythu on 9/4/2015.
 */

public class MessageApplication extends HSApplication implements INotificationObserver {

    /*
     * 同步好友列表的服务器 URL
     */
    public static final String URL_SYNC = "http://54.223.212.19:8024/template/contacts/friends/get";
    public static final String URL_ACK = "http://54.223.212.19:8024/template/contacts/friends/get";

    private static final String LogTag = MessageApplication.class.getName();

    public HSMessageChangeListener messageChangeListener = new HSMessageChangeListener() {
        @Override
        public void onMessageChanged(HSMessageChangeType changeType, List<HSBaseMessage> messages) {
            if (changeType == HSMessageChangeType.ADDED) {
                EventBus.getDefault().post(new MessageAddEvent(messages));
                for (HSBaseMessage message : messages) {
                    String contactMid = message.getFrom();
                    if (contactMid.equals(HSAccountManager.getInstance().getMainAccount().getMID())) {
                        contactMid = message.getTo();
                    }
                    SessionDBManager.MessageSessionInfo session = new SessionDBManager.MessageSessionInfo(
                        contactMid,
                        message.getMsgID(),
                        message.getTimestamp()
                    );
                    if (SessionDBManager.isContactSessionExist(contactMid)) {
                        SessionDBManager.setNewMessage(contactMid, message.getMsgID(), message.getTimestamp());
                    } else {
                        SessionDBManager.insertSession(session);
                    }
                    EventBus.getDefault().post(new SessionUpdateEvent(new MessageSession(session)));
                }
            } else if (changeType == HSMessageChangeType.UPDATED) {
                EventBus.getDefault().post(new MessageUpdateEvent(messages));
            }
        }

        @Override
        public void onTypingMessageReceived(String fromMid) {

        }

        @Override
        public void onOnlineMessageReceived(HSOnlineMessage message) {

        }

        @Override
        public void onUnreadMessageCountChanged(String mid, int newCount) {
            EventBus.getDefault().post(new SessionUnreadCountChangeEvent(mid, newCount));
        }

        @Override
        public void onReceivingRemoteNotification(JSONObject pushInfo) {
            if (HSSessionMgr.getTopActivity() != null) {
                return;
            }
            String contactMid = "";
            String message = "";
            try {
                contactMid = pushInfo.getString("fmid");
                message = pushInfo.getJSONObject("aps").getString("alert");
            } catch (Exception e) {

            }
            String title = FriendManager.getInstance().getFriend(contactMid).getName();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(Uri.parse("android.resource://me.hqythu.ihs.message/ras/message_ringtone_received"))
                .setAutoCancel(true);
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra(ChatActivity.CHAT_MID, contactMid);
            PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                    getApplicationContext(),
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(Integer.parseInt(contactMid), mBuilder.build());

            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(500);
        }
    };

    private static MessageApplication instance = null;

    public static MessageApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;

        super.onCreate();

        HSAccountManager.getInstance();

        doInit();

        initImageLoader(this);

        // 初始化百度地图 SDK
        SDKInitializer.initialize(getApplicationContext());

        // 初始化通讯录管理类，同步通讯录，用于生成好友列表
        HSPhoneContactMgr.init();
        HSPhoneContactMgr.enableAutoUpload(true);
        HSPhoneContactMgr.startSync();

        // 初始化好友列表管理类，同步好友列表
        HSContactFriendsMgr.init(this, null, URL_SYNC, URL_ACK);
        HSContactFriendsMgr.startSync(true);

        // 将本类添加为 HSMessageManager 的监听者，监听各类消息变化事件
        // 参见 HSMessageManager 类与 HSMessageChangeListener 接口
//        HSMessageManager.getInstance().addListener(this, new Handler());

        // 为 HSGlobalNotificationCenter 功能设定监听接口
        INotificationObserver observer = this;

        // 演示HSGlobalNotificationCenter功能：增加名为 SAMPLE_NOTIFICATION_NAME 的观察者
        HSGlobalNotificationCenter.addObserver(SampleFragment.SAMPLE_NOTIFICATION_NAME, observer);
        HSGlobalNotificationCenter.addObserver(FriendManager.NOTIFICATION_NAME_FRIEND_CHANGED,
            new INotificationObserver() {
                @Override
                public void onReceive(String s, HSBundle hsBundle) {
                    EventBus.getDefault().post(new FriendUpdateEvent());
                }
            });

        HSMessageManager.getInstance().addListener(messageChangeListener, new Handler());
    }

    @Override
    public void onTerminate() {
        HSMessageManager.getInstance().removeListener(messageChangeListener);
        super.onTerminate();
    }

    public static void doInit() {
        Log.d(LogTag, "doInit invoked");

        // 验证登录状态
        if (HSAccountManager.getInstance().getSessionState() == HSAccountManager.HSAccountSessionState.VALID) {
            Log.d(LogTag, "doInit during session is valid");
            HSMessageManager.getInstance();

            // 初始化长连接服务管理类 HSKeepCenter
            // 需传入标记应用的 App ID、标记帐户身份的 mid 和标记本次登录的 Session ID，三项信息均可从 HSAccountManager 获得
            HSKeepCenter.getInstance().set(HSAccountManager.getInstance().getAppID(), HSAccountManager.getInstance().getMainAccount().getMID(),
                HSAccountManager.getInstance().getMainAccount().getSessionID());
            // 建立长连接
            HSKeepCenter.getInstance().connect();
        }
    }

    /**
     * 返回配置文件名
     */
    @Override
    protected String getConfigFileName() {
        return "config.ya";
    }

    private static void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 返回多媒体消息的文件存储路径
     */
    void getMediaFilePath() {
        Log.d("getMediaFilePath: ", Utils.getMediaPath());
    }

    /**
     * 收到推送通知时的回调方法
     */
    @Override
    public void onReceive(String notificaitonName, HSBundle bundle) {
        // 供 HSGlobalNotificationCenter 功能参考，弹出 Toast 演示通知的效果
        String string = TextUtils.isEmpty(bundle.getString(SampleFragment.SAMPLE_NOTIFICATION_BUNDLE_STRING)) ? "消息为空" : bundle
            .getString(SampleFragment.SAMPLE_NOTIFICATION_BUNDLE_STRING); // 取得 bundle 中的信息
        Toast toast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG);
        toast.show();
    }
}
