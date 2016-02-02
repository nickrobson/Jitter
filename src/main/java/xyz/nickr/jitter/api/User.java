package xyz.nickr.jitter.api;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;

/**
 * Represents a Gitter User.
 *
 * @author Nick Robson
 */
public interface User {

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
     * Gets this User's ID.
     *
     * @return The ID.
     */
    String getID();

    /**
     * Gets this User's username.
     *
     * @return The username.
     */
    String getUsername();

    /**
     * Gets this User's display name.
     *
     * @return The display name.
     */
    String getDisplayName();

    /**
     * Gets this User's URL.
     *
     * @return The URL.
     */
    String getURL();

    /**
     * Gets this User's small-size avatar URL.
     *
     * @return The avatar URL.
     */
    String getSmallAvatarURL();

    /**
     * Gets this User's medium-size avatar URL.
     *
     * @return The avatar URL.
     */
    String getMediumAvatarURL();

    /**
     * Send this User a message.
     *
     * @param message The {@link Message message}.
     *
     * @return The message.
     */
    Message sendMessage(String message);

}
