package xyz.nickr.jitter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.rjeschke.txtmark.Processor;
import com.mashape.unirest.http.exceptions.UnirestException;

import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.User;
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

    User user;

    final JitterRequests requests;

    final JitterStream stream;
    final JitterBayeux bayeux;
    final JitterPoller poller;
    final JitterEvents events;

    final Map<String, Room> rooms = new HashMap<>();

    Jitter(String token, String api_url) {
        this.token = Objects.requireNonNull(token, "token");
        this.api_url = Objects.requireNonNull(api_url, "api url");
        this.requests = new JitterRequests(this);

        this.stream = new JitterStream(this);
        this.bayeux = new JitterBayeux(this);
        this.poller = new JitterPoller(this);
        this.events = new JitterEvents(this);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Shutting down Bayeux..");
                this.bayeux.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                System.out.println("Shutting down polling..");
                this.poller.stop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }));
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
     * Gets this instance's {@link JitterStream} object.
     *
     * @return The stream object.
     */
    public JitterStream stream() {
        return stream;
    }

    /**
     * Gets this instance's {@link JitterBayeux} object.
     *
     * @return The bayeux object.
     */
    public JitterBayeux bayeux() {
        return bayeux;
    }

    /**
     * Gets this instance's {@link JitterPoller} object.
     *
     * @return The poller object.
     */
    public JitterPoller poller() {
        return poller;
    }

    /**
     * Gets this instance's {@link JitterEvents} object.
     *
     * @return The events object.
     */
    public JitterEvents events() {
        return events;
    }

    /**
     * Gets the authenticated {@link User user} object.
     *
     * @return The user, or null if an error occurred.
     */
    public User getCurrentUser() {
        try {
            if (user != null)
                return user;
            JSONArray arr = requests().get("/user").asJson().getBody().getArray();
            return user = new UserImpl(this, arr.getJSONObject(0));
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
            for (int i = 0, j = arr.length(); i < j; i++) {
                JSONObject o = arr.getJSONObject(i);
                String id = o.getString("id");
                if (!this.rooms.containsKey(id))
                    this.rooms.put(id, new RoomImpl(this, o));
                Room room = this.rooms.get(id);
                if (room.isMember())
                    rooms.add(room);
            }
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
            if (rooms.containsKey(id))
                return rooms.get(id);
            JSONObject o = requests().get("/rooms/" + id).asJson().getBody().getObject();
            return rooms.computeIfAbsent(id, i -> new RoomImpl(this, o));
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
    public String toHTML(String message) {
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
