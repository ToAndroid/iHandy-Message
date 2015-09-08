package me.hqythu.ihs.message.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import me.hqythu.ihs.message.MessageApplication;

/**
 * Created by hqythu on 9/8/2015.
 */

public class SessionDBManager {
    private static final String DB_NAME = "HSMessage.Session";
    private static final int DB_VERSION = 1;

    private static final String SESSION_TABLE_NAME = "SessionTable";
    private static final String COLUMN_CONTACT_MID = "ContactMid";
    private static final String COLUMN_LAST_MESSAGE_DATE = "LastMessageDate";
    private static final String COLUMN_LAST_MESSAGE_MID = "LastMessageMid";
    private static final String COLUMN_ARCHIVED = "Archived";
    private static final String COLUMN_SNOOZE_DATE = "SnoozeDate";

    private static volatile SQLiteDatabase mSQLiteDatabase = null;
    private static volatile DatabaseHelper mDatabaseHelper = null;

    public static class MessageSessionInfo {
        public String contactMid;
        public String lastMessageMid;
        public Date lastMessageDate;
        public boolean archived;
        public Date snoozeDate;

        public MessageSessionInfo(String contactMid, String lastMessageMid, Date lastMessageDate,
                                  boolean archived, Date snoozeDate) {
            this.contactMid = contactMid;
            this.lastMessageDate = lastMessageDate;
            this.lastMessageMid = lastMessageMid;
            this.archived = archived;
            this.snoozeDate = snoozeDate;
        }

        public MessageSessionInfo(String contactMid, String lastMessageMid, Date lastMessageDate) {
            this.contactMid = contactMid;
            this.lastMessageDate = lastMessageDate;
            this.lastMessageMid = lastMessageMid;
            this.archived = false;
            this.snoozeDate = null;
        }

        public ContentValues getDBInfo() {
            ContentValues initialValues = new ContentValues();
            initialValues.put(COLUMN_CONTACT_MID, contactMid);
            initialValues.put(COLUMN_LAST_MESSAGE_MID, lastMessageMid);
            if (lastMessageDate != null) {
                initialValues.put(COLUMN_LAST_MESSAGE_DATE, lastMessageDate.getTime());
            } else {
                initialValues.put(COLUMN_LAST_MESSAGE_DATE, 0);
            }
            initialValues.put(COLUMN_ARCHIVED, archived ? 1 : 0);
            if (snoozeDate != null) {
                initialValues.put(COLUMN_SNOOZE_DATE, snoozeDate.getTime());
            } else {
                initialValues.put(COLUMN_SNOOZE_DATE, 0);
            }
            return initialValues;
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DB_CREATE_SESSION_SQL = "CREATE TABLE IF NOT EXISTS "
            + SESSION_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CONTACT_MID + " TEXT, "
            + COLUMN_LAST_MESSAGE_MID + " TEXT, "
            + COLUMN_LAST_MESSAGE_DATE + " INTEGER, "
            + COLUMN_ARCHIVED + " INTEGER, "
            + COLUMN_SNOOZE_DATE + " INTEGER)";

        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_SESSION_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE_NAME);
            onCreate(db);
        }
    }

    private static synchronized void checkDatabase() {
        if (mSQLiteDatabase == null) {
            mDatabaseHelper = new DatabaseHelper(MessageApplication.getContext());
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        }
    }

    public static boolean insertSession(MessageSessionInfo info) {
        checkDatabase();
        return mSQLiteDatabase.insert(SESSION_TABLE_NAME, null, info.getDBInfo()) > 0;
    }

    public static boolean updateSessionInfo(String contactMid, MessageSessionInfo newInfo) {
        checkDatabase();
        return mSQLiteDatabase.update(SESSION_TABLE_NAME, newInfo.getDBInfo(),
            COLUMN_CONTACT_MID + "=" + contactMid, null) > 0;
    }

    public static boolean setArchived(String contactMid, boolean archived) {
        checkDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_ARCHIVED, archived ? 1 : 0);
        return mSQLiteDatabase.update(SESSION_TABLE_NAME, initialValues,
            COLUMN_CONTACT_MID + "=" + contactMid, null) > 0;
    }

    public static boolean setNewMessage(String contactMid, String lastMessageMid, Date lastMessageDate) {
        checkDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_LAST_MESSAGE_MID, lastMessageMid);
        if (lastMessageDate != null) {
            initialValues.put(COLUMN_LAST_MESSAGE_DATE, lastMessageDate.getTime());
        } else {
            initialValues.put(COLUMN_LAST_MESSAGE_DATE, 0);
        }
        return mSQLiteDatabase.update(SESSION_TABLE_NAME, initialValues,
            COLUMN_CONTACT_MID + "=" + contactMid, null) > 0;
    }

    public static boolean setSnoozeDate(String contactMid, Date snoozeDate) {
        checkDatabase();
        ContentValues initialValues = new ContentValues();
        if (snoozeDate != null) {
            initialValues.put(COLUMN_SNOOZE_DATE, snoozeDate.getTime());
        } else {
            initialValues.put(COLUMN_SNOOZE_DATE, 0);
        }
        return mSQLiteDatabase.update(SESSION_TABLE_NAME, initialValues,
            COLUMN_CONTACT_MID + "=" + contactMid, null) > 0;
    }
}
