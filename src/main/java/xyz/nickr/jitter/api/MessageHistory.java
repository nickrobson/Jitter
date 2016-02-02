package xyz.nickr.jitter.api;

import java.util.Date;
import java.util.List;

/**
 * Represents a {@link Room}'s messages.
 *
 * @author Nick Robson
 */
public interface MessageHistory {

    /**
     * Gets the parent room.
     *
     * @return The room.
     */
    Room getRoom();

    /**
     * Gets all loaded messages.
     *
     * @return The messages.
     */
    List<Message> getMessages();

    /**
     * Loads the next {@code n} messages.
     *
     * @param n The number of messages.
     *
     * @return The messages loaded.
     */
    List<Message> loadMessages(int n);

    /**
     * Loads as many messages as it can (until it runs out of messages to fetch).
     *
     * @return The messages loaded.
     */
    List<Message> fullyLoad();

    /**
     * Gets a portion of this history, consisting of messages sent BEFORE a certain date.
     *
     * @param date The date.
     *
     * @return The portion of history.
     */
    MessageHistory before(Date date);

    /**
     * Gets a portion of this history, consisting of messages sent AFTER a certain date.
     *
     * @param date The date.
     *
     * @return The portion of history.
     */
    MessageHistory after(Date date);

}
