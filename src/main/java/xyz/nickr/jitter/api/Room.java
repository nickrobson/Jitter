package xyz.nickr.jitter.api;

import java.util.List;
import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;

/**
 * Represents a Gitter room or channel.
 *
 * @author Nick Robson
 */
public interface Room {

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
     * Gets this room's ID.
     *
     * @return The ID.
     */
    String getID();

    /**
     * Gets this room's name.
     *
     * @return The name.
     */
    String getName();

    /**
     * Gets this room's topic.
     *
     * @return The topic.
     */
    String getTopic();

    /**
     * Gets this room's type.
     *
     * @return The type.
     */
    RoomType getType();

    /**
     * Gets this room's URI.
     *
     * @return The URI.
     */
    String getURI();

    /**
     * Gets this room's user count.
     *
     * @return The user count.
     */
    int getUserCount();

    /**
     * Gets the number of unread messages in this room.
     *
     * @return The count.
     */
    int getUnreadItems();

    /**
     * Gets the number of unread mentions in this room.
     *
     * @return The count.
     */
    int getUnreadMentions();

    /**
     * Gets the time this room was last accessed.
     *
     * @return The time it was last accessed.
     */
    String getLastAccessTime();

    /**
     * Gets whether or not the current user is a member of this room.
     *
     * @return True if it is; false otherwise.
     */
    boolean isMember();

    /**
     * Gets whether or not this room is marked as a favourite room.
     *
     * @return True if it is; false otherwise.
     */
    boolean isFavourite();

    /**
     * Gets whether or not the authenticated is lurking in this room.
     *
     * @return True if it is; false otherwise.
     */
    boolean isLurkEnabled();

    /**
     * Gets this room's URL path.
     *
     * @return The URL path.
     */
    String getPathURL();

    /**
     * Gets this room's tags.
     *
     * @return The tags.
     */
    List<String> getTags();

    /**
     * Gets this room's version.
     *
     * @return The version.
     */
    int getVersion();

    /**
     * Gets this room's subchannels.
     *
     * @return The subchannels.
     */
    List<Room> getChannels();

    /**
     * Loads and returns this room's users.
     * <br><br>
     * Note: This is a potentially costly operation if there are lots of users.
     * Caching is a good idea for this.
     *
     * @return The users.
     */
    List<RoomUser> getUsers();

    /**
     * Gets the {@link Message} of a given ID.
     *
     * @param id The ID.
     *
     * @return The message, or null if no such message.
     */
    Message getMessage(String id);

    /**
     * Gets this room's message history.
     *
     * @return The message history;
     */
    MessageHistory getMessageHistory();

    /**
     * Gets this room's message history, with messages before and including the given message.
     *
     * @param message The message.
     *
     * @return The message history.
     */
    MessageHistory getMessagesBefore(Message message);

    /**
     * Gets this room's message history, with messages after and including the given message.
     *
     * @param message The message.
     *
     * @return The message history.
     */
    MessageHistory getMessagesAfter(Message message);

    /**
     * Sends a message to this room.
     *
     * @param message The message.
     *
     * @return The message.
     */
    Message sendMessage(String message);

    /**
     * Makes this room start streaming messages through the event API.
     */
    default void beginMessageStreaming() {
        getJitter().stream().beginMessagesStream(this);
    }

    /**
     * Makes this room start streaming room events through the event API.
     */
    default void beginEventsStreaming() {
        getJitter().stream().beginEventsStream(this);
    }

    /**
     * Updates this Room with the given JSON data.
     *
     * @param data The data.
     */
    void update(JSONObject data);

}
