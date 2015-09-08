package me.hqythu.ihs.message.event;

import me.hqythu.ihs.message.data.MessageSession;

/**
 * Created by hqythu on 9/8/2015.
 */
public class SessionUpdateEvent extends SessionEvent {
    private MessageSession session;

    public SessionUpdateEvent(MessageSession session) {
        this.session = session;
    }

    public MessageSession getSession() {
        return session;
    }
}
