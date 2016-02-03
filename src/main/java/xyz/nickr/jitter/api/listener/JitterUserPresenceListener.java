package xyz.nickr.jitter.api.listener;

import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.User;
import xyz.nickr.jitter.api.UserPresenceEvent;

/**
 * Represents a listener for when a {@link Message message} is sent to a {@link Room room} by a {@link User user}.
 *
 * @author Nick Robson
 */
public interface JitterUserPresenceListener {

    /**
     * Called when a user's presence changes in a room.
     *
     * @param event The event.
     */
    void onUserPresence(UserPresenceEvent event);

}
