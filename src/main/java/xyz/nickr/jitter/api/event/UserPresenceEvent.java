package xyz.nickr.jitter.api.event;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;

/**
 * Represents an event of when a user starts or stops watching a room.
 *
 * @author Nick Robson
 */
public interface UserPresenceEvent {

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
     * Gets the room that the user started/stopped watching.
     *
     * @return The room.
     */
    Room getRoom();

    /**
     * Gets the user ID of who is watching.
     *
     * @return The user ID.
     */
    String getUserID();

    /**
     * Gets whether or not the user has just started watching.
     *
     * @return True if they have just started; false otherwise.
     */
    boolean isIncoming();

}
