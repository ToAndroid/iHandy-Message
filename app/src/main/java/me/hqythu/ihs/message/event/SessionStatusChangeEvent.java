package me.hqythu.ihs.message.event;

import me.hqythu.ihs.message.data.MessageSession;

/**
 * Created by hqythu on 9/8/2015.
 */
public class SessionStatusChangeEvent extends SessionEvent {
    private MessageSession session;
    private int type;

    public SessionStatusChangeEvent(MessageSession session, int type) {
        this.session = session;
        this.type = type;
    }

    public MessageSession getSession() {
        return session;
    }

    public int getType() {
        return type;
    }
}
