package xyz.nickr.jitter.api;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;

/**
 * Represents a message sent to a {@link Room room} by a {@link User user}.
 *
 * @author Nick Robson
 */
public interface Message {

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
     * Gets the {@link Room} this message was sent to.
     *
     * @return The room.
     */
    Room getRoom();

    /**
     * Gets the {@link User} this message was sent by.
     *
     * @return The user.
     */
    User getSender();

    /**
     * Gets this message's ID.
     *
     * @return The ID.
     */
    String getID();

    /**
     * Gets this message's text in Markdown.
     *
     * @return The text.
     */
    String getText();

    /**
     * Gets this message's text in HTML.
     *
     * @return The message's HTML text.
     */
    String getHtml();

    /**
     * Gets the {@link Date} when this message was sent.
     *
     * @return The date when it was sent.
     */
    Date getSentTimestamp();

    /**
     * Gets the {@link Date} when this message was last edited, if it ever has been.
     *
     * @return The date when it was edited.
     */
    Optional<Date> getEditTimestamp();

    /**
     * Edits this message to contain the given text.
     * This object is updated to reflect the changes.
     *
     * @param message The new message.
     */
    void edit(String message);

    /**
     * Gets whether or not the authenticated user has read this message.
     * <br><br>
     * <em>NOTE: API queries don't count as reading.</em>
     *
     * @return Whether or not this message has been read.
     */
    boolean isRead();

    /**
     * Gets the number of people who have read this message.
     *
     * @return The number of read messages.
     */
    int getReadCount();

    /**
     * Gets any URLs mentioned in this message.
     *
     * @return The URLs.
     */
    List<String> getURLs();

    /**
     * Gets any users mentioned in this message.
     *
     * @return The users.
     */
    List<MentionedUser> getMentions();

    /**
     * Gets any issues mentioned in this message.
     *
     * @return The issues.
     */
    List<MentionedIssue> getIssues();

    /**
     * Gets this message's {@link MessageMetadata metadata}.
     *
     * @return The metadata.
     */
    MessageMetadata getMetadata();

    /**
     * Gets this message's version.
     *
     * @return The version.
     */
    int getVersion();

}
