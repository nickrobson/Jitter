package xyz.nickr.jitter.api.listener;

import xyz.nickr.jitter.api.event.UserJoinEvent;

/**
 * Represents a listener for when a user joins a room.
 *
 * @author Nick Robson
 */
public interface JitterUserJoinListener {

    /**
     * Called when a user joins a room.
     *
     * @param event The event.
     */
    void onJoin(UserJoinEvent event);

}
