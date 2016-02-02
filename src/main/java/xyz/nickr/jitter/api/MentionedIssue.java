package xyz.nickr.jitter.api;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;

/**
 * Represents a user mentioned using {@code #number} in a {@link Message message}.
 *
 * @author Nick Robson
 */
public interface MentionedIssue {

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
     * Gets the {@link Message} in which this mention took place.
     *
     * @return The message.
     */
    Message getMessage();

    /**
     * Gets the issue number mentioned.
     *
     * @return The issue number.
     */
    int getNumber();

}
