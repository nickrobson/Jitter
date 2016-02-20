package xyz.nickr.jitter.api.event;

import org.json.JSONObject;

import xyz.nickr.jitter.api.Room;

/**
 * Represents an event of when the current user "reads" messages.
 *
 * @author Nick Robson
 */
public interface RoomReadMessagesEvent extends JitterEvent {

    /**
     * Gets the underlying JSON object.
     *
     * @return The JSON.
     */
    JSONObject asJSON();

    /**
     * Gets the message identifiers.
     *
     * @return The identifiers.
     */
    String[] getMessageIds();

    /**
     * Gets whether or not this event represents new messages or reading old messages.
     *
     * @return True if new messages, false otherwise.
     */
    boolean isUnread();

    /**
     * Gets the room in which this event occurred.
     *
     * @return The room.
     */
    Room getRoom();

}
