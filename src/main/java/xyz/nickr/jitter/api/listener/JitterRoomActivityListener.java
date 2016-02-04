package xyz.nickr.jitter.api.listener;

import xyz.nickr.jitter.api.event.RoomActivityEvent;

/**
 * Represents a listener for when a room has activity.
 *
 * @author Nick Robson
 */
public interface JitterRoomActivityListener {

    /**
     * Called when a room has activity.
     *
     * @param event The event.
     */
    void onActivity(RoomActivityEvent event);

}