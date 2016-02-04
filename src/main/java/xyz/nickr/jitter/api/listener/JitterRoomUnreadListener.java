package xyz.nickr.jitter.api.listener;

import xyz.nickr.jitter.api.event.RoomUnreadEvent;

/**
 * Represents a listener for when a room has a changing amount of unread messages.
 *
 * @author Nick Robson
 */
public interface JitterRoomUnreadListener {

    /**
     * Called when a room has a changing amount of unread messages.
     *
     * @param event The event.
     */
    void onUnread(RoomUnreadEvent event);

}