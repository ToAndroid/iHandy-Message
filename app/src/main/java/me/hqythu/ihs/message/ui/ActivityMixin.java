package me.hqythu.ihs.message.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author guangchen.
 */
public class ActivityMixin {
    /** Start other activity with given intent.
     * @param activity activity from
     * @param intent intent to
     * @param isFinish finish current activity or not
     */
    public static void startOtherActivity(Activity activity, Intent intent, boolean isFinish) {
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
    }

    /**
     * Start other activity.
     * @param activityFrom from which activity
     * @param activityTo start which activity
     * @param isFinish finish current activity or not
     */
    public static void startOtherActivity(Activity activityFrom, Activity activityTo, boolean isFinish) {
        final Class<? extends Activity> activityToClass = activityTo.getClass();
        startOtherActivity(activityFrom, activityToClass, isFinish);
    }

    /**
     * Start other activity.
     * @param activityFrom from which activity
     * @param activityToClass to which activity
     * @param isFinish finish current activity or not
     */
    public static void startOtherActivity(Activity activityFrom, Class<? extends Activity> activityToClass, boolean isFinish) {
        Intent intent = new Intent(activityFrom, activityToClass);
        startOtherActivity(activityFrom, intent, isFinish);
    }

    /**
     * Start other activity and won't finish current activity.
     * @param activityFrom fro which activity
     * @param activityTo start which activity
     */
    public static void startOtherActivity(Activity activityFrom, Activity activityTo) {
        startOtherActivity(activityFrom, activityTo, false);
    }

    public static void startOtherActivity(Activity activityFrom, Class<? extends Activity> activityToClass) {
        startOtherActivity(activityFrom, activityToClass, false);
    }

    /**
     * Start other activity for result.
     * @param activityFrom from which activity
     * @param activityTo start activity for result
     * @param requestCode request code
     */
    public static void startOtherActivityForResult(Activity activityFrom, Activity activityTo, int requestCode) {
        startOtherActivityForResult(activityFrom, activityTo, null, requestCode);
    }

    /**
     * Start other activity for result.
     * @param activityFrom from which activity
     * @param activityTo start which activity
     * @param data transmit bundle data
     * @param requestCode request code for result
     */
    public static void startOtherActivityForResult(Activity activityFrom, Activity activityTo, Bundle data, int requestCode) {
        final Class<? extends Activity> activityToClass = activityTo.getClass();
        startOtherActivityForResult(activityFrom, activityToClass, data, requestCode);
    }

    /**
     * Start other activity for result.
     * @param activityFrom from
     * @param activityToClass to
     * @param data data
     * @param requestCode requestCode
     */
    public static void startOtherActivityForResult(Activity activityFrom, Class<? extends Activity> activityToClass, Bundle data, int requestCode) {
        Intent intent = new Intent(activityFrom, activityToClass);
        if (data != null) {
            intent.putExtras(data);
        }
        activityFrom.startActivityForResult(intent, requestCode);
    }
}
