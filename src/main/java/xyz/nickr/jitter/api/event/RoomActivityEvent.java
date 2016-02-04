package xyz.nickr.jitter.api.event;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;

/**
 * Represents an event of when a room has activity, usually when someone sends a message.
 *
 * @author Nick Robson
 */
public interface RoomActivityEvent {

    /**
     * Gets the providing {@link Jitter} object.
     *
     * @return The provider.
     */
    Jitter getJitter();

    /**
     * Gets the underlying JSON object.
     *
     * @return The JSON.
     */
    JSONObject asJSON();

    /**
     * Gets the room in which this event occurred.
     *
     * @return The room.
     */
    Room getRoom();

}
