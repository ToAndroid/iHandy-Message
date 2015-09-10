package me.hqythu.ihs.message.data;

import com.ihs.demo.message.Contact;
import com.ihs.demo.message.FriendManager;
import com.ihs.message_2012010548.managers.HSMessageManager;
import com.ihs.message_2012010548.types.HSBaseMessage;
import com.ihs.message_2012010548.types.HSImageMessage;
import com.ihs.message_2012010548.types.HSTextMessage;

import me.hqythu.ihs.message.db.SessionDBManager;

/**
 * Created by hqythu on 9/8/2015.
 */
public class MessageSession extends SessionDBManager.MessageSessionInfo {
    public Contact contact;
    public String messageBrief;
    public int unreadCount;

    public MessageSession(SessionDBManager.MessageSessionInfo sessionInfo) {
        super(sessionInfo.contactMid, sessionInfo.lastMessageMid,
            sessionInfo.lastMessageDate, sessionInfo.archived, sessionInfo.snoozeDate);
        contact = FriendManager.getInstance().getFriend(contactMid);
        HSBaseMessage message = HSMessageManager.getInstance().queryMessage(lastMessageMid);
        String messageBrief;
        if (message instanceof HSTextMessage) {
            messageBrief = ((HSTextMessage)message).getText();
        } else if (message instanceof HSImageMessage) {
            // TODO i18n
            messageBrief = "[Image]";
        } else {
            messageBrief = "Unsupported Message Type";
        }
        this.messageBrief = messageBrief;
        this.unreadCount = HSMessageManager.getInstance().queryUnreadCount(contactMid);
    }

    public void updateContact() {
        contact = FriendManager.getInstance().getFriend(contactMid);
    }

    @Override
    public boolean equals(Object session) {
        if (session instanceof MessageSession) {
            return contactMid.equals(((MessageSession)session).contactMid);
        } else {
            return false;
        }
    }
}
