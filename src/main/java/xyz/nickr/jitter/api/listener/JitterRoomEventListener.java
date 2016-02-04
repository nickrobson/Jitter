package xyz.nickr.jitter.api.listener;

import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.event.RoomEvent;

/**
 * Represents a listener for when an {@link RoomEvent event} is made on a {@link Room room}.
 *
 * @author Nick Robson
 */
public interface JitterRoomEventListener {

    /**
     * Called when an event is made on a room.
     *
     * @param event The event.
     */
    void onEvent(RoomEvent event);

}
