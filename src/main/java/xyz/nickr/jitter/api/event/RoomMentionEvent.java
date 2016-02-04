package xyz.nickr.jitter.api.event;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;

/**
 * Represents an event of when a room has a changing amount unread messages.
 *
 * @author Nick Robson
 */
public interface RoomMentionEvent {

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
     * Gets the number of mentions, can be 0 indicating all mentions have been read.
     *
     * @return The number.
     */
    int getMentions();

    /**
     * Gets the room in which this event occurred.
     *
     * @return The room.
     */
    Room getRoom();

}
