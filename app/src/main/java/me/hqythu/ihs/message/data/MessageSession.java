package me.hqythu.ihs.message.data;

import com.ihs.demo.message.Contact;
import com.ihs.demo.message.FriendManager;

import java.util.Date;

import me.hqythu.ihs.message.db.SessionDBManager;

/**
 * Created by hqythu on 9/8/2015.
 */
public class MessageSession extends SessionDBManager.MessageSessionInfo {
    public Contact contact;

    public MessageSession(SessionDBManager.MessageSessionInfo sessionInfo) {
        super(sessionInfo.contactMid, sessionInfo.lastMessageMid,
            sessionInfo.lastMessageDate, sessionInfo.archived, sessionInfo.snoozeDate);
        contact = FriendManager.getInstance().getFriend(contactMid);
    }
}
