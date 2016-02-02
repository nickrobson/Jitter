package xyz.nickr.jitter.api.listener;

import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.User;

/**
 * Represents a listener for when a {@link Message message} is sent to a {@link Room room} by a {@link User user}.
 *
 * @author Nick Robson
 */
public interface JitterMessageListener {

    /**
     * Called when a message is sent to a room.
     *
     * @param message The message.
     */
    void onMessage(Message message);

}
