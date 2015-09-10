package me.hqythu.ihs.message.event;

import me.hqythu.ihs.message.data.MessageSession;

/**
 * Created by hqythu on 9/10/2015.
 */
public class SessionDeleteEvent extends SessionEvent {
    private MessageSession session;

    public SessionDeleteEvent(MessageSession session) {
        this.session = session;
    }

    public MessageSession getSession() {
        return session;
    }
}
