package xyz.nickr.jitter.api.event;

import xyz.nickr.jitter.api.Message;

/**
 * Represents an event of when a message is sent.
 *
 * @author Nick Robson
 */
public interface MessageReceivedEvent extends JitterEvent {

    /**
     * Gets the message involved in the event.
     *
     * @return The message.
     */
    Message getMessage();

}
