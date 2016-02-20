package xyz.nickr.jitter.impl.event;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.event.MessageReceivedEvent;

public class MessageReceivedEventImpl implements MessageReceivedEvent {

    private final Message message;

    public MessageReceivedEventImpl(Message message) {
        this.message = message;
    }

    @Override
    public Jitter getJitter() {
        return message.getJitter();
    }

    @Override
    public Message getMessage() {
        return message;
    }

}
