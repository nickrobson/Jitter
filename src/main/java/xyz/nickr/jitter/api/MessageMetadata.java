package xyz.nickr.jitter.api;

import org.json.JSONArray;

import xyz.nickr.jitter.Jitter;

/**
 * Represents a {@link Message message}'s metadata.
 *
 * @author Nick Robson
 */
public interface MessageMetadata {

    /**
     * Gets the providing {@link Jitter} object.
     *
     * @return The provider.
     */
    Jitter getJitter();

    /**
     * Gets the underlying JSON array.
     *
     * @return The JSON.
     */
    JSONArray asJSON();

    /**
     * Gets the {@link Message} in which this mention took place.
     *
     * @return The message.
     */
    Message getMessage();

}
