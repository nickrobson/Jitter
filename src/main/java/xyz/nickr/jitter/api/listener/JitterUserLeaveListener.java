package xyz.nickr.jitter.api.listener;

import xyz.nickr.jitter.api.event.UserLeaveEvent;

/**
 * Represents a listener for when a user leaves a room.
 *
 * @author Nick Robson
 */
public interface JitterUserLeaveListener {

    /**
     * Called when a user leaves a room.
     *
     * @param event The event.
     */
    void onLeave(UserLeaveEvent event);

}
