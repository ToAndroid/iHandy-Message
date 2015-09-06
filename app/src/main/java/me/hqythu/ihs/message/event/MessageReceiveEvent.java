package me.hqythu.ihs.message.event;

import com.ihs.message_2012010548.types.HSBaseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hqythu on 9/6/2015.
 */
public class MessageReceiveEvent extends MessageEvent {

    private ArrayList<HSBaseMessage> messages;

    public MessageReceiveEvent(List<HSBaseMessage> messages) {
        this.messages = new ArrayList<>(messages);
    }

    public ArrayList<HSBaseMessage> getMessages() {
        return messages;
    }
}
