package xyz.nickr.jitter.api.event;

import java.util.Date;
import java.util.Optional;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.User;

/**
 * Represents an event that is broadcast in a room.
 * <br><br>
 * For example, if issue tracking is enabled in the room, an event is called when an event is opened/closed/reopened/tagged.
 *
 * @author Nick Robson
 */
public interface RoomEvent {

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
     * Gets the event's ID.
     *
     * @return The ID.
     */
    String getID();

    /**
     * Gets the name of the service that called the event.
     *
     * @return The service name.
     */
    String getService();

    /**
     * Gets the action that the event describes.
     *
     * @return The action.
     */
    String getAction();

    /**
     * Gets the text representation of the event.
     *
     * @return The text.
     */
    String getText();

    /**
     * Gets the HTML representation of the event.
     *
     * @return The HTML.
     */
    String getHtml();

    /**
     * Gets the room in which this event occurred.
     *
     * @return The room.
     */
    Room getRoom();

    /**
     * Gets the date when this event was sent.
     *
     * @return The date.
     */
    Date getSentTimestamp();

    /**
     * Gets the date when this event was edited.
     *
     * @return The date, if it exists.
     */
    Optional<Date> getEditTimestamp();

    /**
     * Gets the user that triggered the event.
     *
     * @return The user, if existent.
     */
    Optional<User> getUser();

    /**
     * Gets the JSON object describing this event's metadata.
     *
     * @return The metadata.
     */
    JSONObject getMetadata();

    /**
     * Gets the JSON object describing this event's payload.
     *
     * @return The payload.
     */
    JSONObject getPayload();

    /**
     * Gets this event's version.
     *
     * @return The version.
     */
    int getVersion();

}
