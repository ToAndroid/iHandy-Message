package me.hqythu.ihs.message;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.ihs.account.api.account.HSAccountManager;
import com.ihs.app.framework.HSApplication;
import com.ihs.commons.keepcenter.HSKeepCenter;
import com.ihs.commons.notificationcenter.HSGlobalNotificationCenter;
import com.ihs.commons.notificationcenter.INotificationObserver;
import com.ihs.commons.utils.HSBundle;
import com.ihs.commons.utils.HSLog;
import com.ihs.contacts.api.HSPhoneContactMgr;
import com.ihs.demo.message.SampleFragment;
import com.ihs.message_2012010548.managers.HSMessageManager;
import com.ihs.message_2012010548.utils.Utils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import test.contacts.demo.friends.api.HSContactFriendsMgr;
//import android.support.multidex.MultiDexApplication;

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

    @Override
    public void onCreate() {
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
    }

    private static void doInit() {
        HSLog.d(LogTag, "doInit invoked");

        // 验证登录状态
        if (HSAccountManager.getInstance().getSessionState() == HSAccountManager.HSAccountSessionState.VALID) {
            HSLog.d(LogTag, "doInit during session is valid");
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
        HSLog.d("getMediaFilePath: ", Utils.getMediaPath());
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
