package xyz.nickr.jitter.api.event;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.User;

/**
 * Represents an event of when a user joins a room.
 *
 * @author Nick Robson
 */
public interface UserJoinEvent {

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
     * Gets the room that the user joined.
     *
     * @return The room.
     */
    Room getRoom();

    /**
     * Gets the user who joined.
     *
     * @return The user.
     */
    User getUser();

}
