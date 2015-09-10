package me.hqythu.ihs.message.event;

/**
 * Created by hqythu on 9/10/2015.
 */
public class SessionUnreadCountChangeEvent extends SessionEvent {
    private int unreadCount;
    private String contactMid;

    public SessionUnreadCountChangeEvent(String contactMid, int newCount) {
        this.contactMid =contactMid;
        this.unreadCount = newCount;
    }

    public String getContactMid() {
        return contactMid;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}
