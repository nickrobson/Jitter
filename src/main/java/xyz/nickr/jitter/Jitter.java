package xyz.nickr.jitter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.rjeschke.txtmark.Processor;
import com.mashape.unirest.http.exceptions.UnirestException;

import xyz.nickr.jitter.api.ListenerSet;
import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.RoomEvent;
import xyz.nickr.jitter.api.User;
import xyz.nickr.jitter.api.listener.JitterEventListener;
import xyz.nickr.jitter.api.listener.JitterMessageListener;
import xyz.nickr.jitter.impl.RoomImpl;
import xyz.nickr.jitter.impl.UserImpl;

/**
 * The main entry-point for Jitter.
 *
 * @author Nick Robson
 */
public final class Jitter {

    public static final Pattern URL_PATTERN = Pattern.compile("(?i)\\b((?:[a-z][\\w-]+:(?:/{1,3}|[a-z0-9%])|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))\\b");

    /**
     * Creates a {@link JitterBuilder} object.
     *
     * @return A new builder.
     */
    public static JitterBuilder builder() {
        return new JitterBuilder();
    }

    public final String token;
    final String api_url;
    final JitterStream stream;
    final JitterRequests requests;

    final ListenerSet<JitterMessageListener> messageListeners;
    final ListenerSet<JitterEventListener> eventListeners;

    Jitter(String token, String api_url) {
        this.token = Objects.requireNonNull(token, "token");
        this.api_url = Objects.requireNonNull(api_url, "api url");
        this.requests = new JitterRequests(this);
        this.stream = new JitterStream(this);

        this.messageListeners = new ListenerSet<>();
        this.eventListeners = new ListenerSet<>();
    }

    /**
     * Gets this instance's {@link JitterStream} object.
     *
     * @return The stream object.
     */
    public JitterStream stream() {
        return stream;
    }

    /**
     * Gets this instance's {@link JitterRequests} object.
     *
     * @return The requests object.
     */
    public JitterRequests requests() {
        return requests;
    }

    /**
     * Calls a {@link Message message} event for all registered {@link JitterMessageListener message listeners}.
     *
     * @param msg The message.
     */
    public void onMessage(Message msg) {
        messageListeners.forEach(l -> l.onMessage(msg));
    }

    /**
     * Calls a {@link RoomEvent room event} for all registered {@link JitterEventListener event listeners}.
     *
     * @param msg The message.
     */
    public void onEvent(RoomEvent event) {
        eventListeners.forEach(l -> l.onEvent(event));
    }

    /**
     * Registers a {@link JitterMessageListener message listener}.
     *
     * @param listener The listener.
     */
    public void onMessage(JitterMessageListener listener) {
        messageListeners.add(listener);
    }

    /**
     * Registers a {@link JitterEventListener event listener}.
     *
     * @param listener The listener.
     */
    public void onEvent(JitterEventListener listener) {
        eventListeners.add(listener);
    }

    /**
     * Gets the authenticated {@link User user} object.
     *
     * @return The user, or null if an error occurred.
     */
    public User getCurrentUser() {
        try {
            JSONArray arr = requests().get("/user").asJson().getBody().getArray();
            return new UserImpl(this, arr.getJSONObject(0));
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a list of all {@link Room rooms} that the authenticated {@link User user} is a member of.
     *
     * @return The rooms, or null if an error occurred.
     */
    public List<Room> getCurrentRooms() {
        try {
            List<Room> rooms = new LinkedList<>();
            JSONArray arr = requests().get("/rooms").asJson().getBody().getArray();
            for (int i = 0, j = arr.length(); i < j; i++)
                rooms.add(new RoomImpl(this, arr.getJSONObject(i)));
            return rooms;
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the {@link Room room} with the given ID.
     *
     * @param id The ID.
     *
     * @return The room, or null if an error occurred.
     */
    public Room getRoom(String id) {
        try {
            JSONObject o = requests().get("/rooms/" + id).asJson().getBody().getObject();
            return new RoomImpl(this, o);
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts Markdown to HTML.
     *
     * @param message The markdown text.
     *
     * @return The HTML text.
     */
    public String toHtml(String message) {
        try {
            Matcher matcher = URL_PATTERN.matcher(message);
            message = matcher.replaceAll("[$1]($1)");
            message = Processor.process(message);
            message = URLEncoder.encode(message, "UTF-8");
            return message;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
