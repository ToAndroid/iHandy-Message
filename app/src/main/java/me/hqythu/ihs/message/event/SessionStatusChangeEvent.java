package me.hqythu.ihs.message.event;

import me.hqythu.ihs.message.data.MessageSession;

/**
 * Created by hqythu on 9/8/2015.
 */
public class SessionStatusChangeEvent extends SessionEvent {
    private MessageSession session;
    private boolean isArchived;

    public SessionStatusChangeEvent(MessageSession session, boolean isArchived) {
        this.session = session;
        this.isArchived = isArchived;
    }

    public MessageSession getSession() {
        return session;
    }

    public boolean getArchived() {
        return isArchived;
    }
}
