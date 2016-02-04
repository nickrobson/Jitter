package xyz.nickr.jitter.api.listener;

import xyz.nickr.jitter.api.event.UserPresenceEvent;

/**
 * Represents a listener for when a user starts or stops watching a room.
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
