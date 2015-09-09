package me.hqythu.ihs.message.event;

import com.ihs.message_2012010548.types.HSBaseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hqythu on 9/6/2015.
 */
public class MessageAddEvent extends MessageEvent {

    private ArrayList<HSBaseMessage> messages;

    public MessageAddEvent(List<HSBaseMessage> messages) {
        this.messages = new ArrayList<>(messages);
    }

    public ArrayList<HSBaseMessage> getMessages() {
        return messages;
    }
}
