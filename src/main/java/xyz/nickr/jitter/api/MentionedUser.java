package xyz.nickr.jitter.api;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;

/**
 * Represents a user mentioned using {@code @user} in a {@link Message message}.
 *
 * @author Nick Robson
 */
public interface MentionedUser {

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
     * Gets the mentioned user's ID.
     *
     * @return The ID.
     */
    String getID();

    /**
     * Gets the mentioned user's display name.
     *
     * @return The display name.
     */
    String getDisplayName();

}
