package xyz.nickr.jitter;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

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

public final class Jitter {

    public static JitterBuilder builder() {
        return new JitterBuilder();
    }

    public final String token;
    final String api_url;
    final JitterStream stream;
    final JitterRequests requests;
    final ExecutorService executor;

    final ListenerSet<JitterMessageListener> messageListeners;
    final ListenerSet<JitterEventListener> eventListeners;

    Jitter(String token, String api_url) {
        this.token = Objects.requireNonNull(token, "token");
        this.api_url = Objects.requireNonNull(api_url, "api url");
        this.executor = Executors.newFixedThreadPool(10);
        this.requests = new JitterRequests(this);
        this.stream = new JitterStream(this);

        this.messageListeners = new ListenerSet<>();
        this.eventListeners = new ListenerSet<>();
    }

    public JitterStream stream() {
        return stream;
    }

    public JitterRequests requests() {
        return requests;
    }

    public ExecutorService executor() {
        return executor;
    }

    public void onMessage(Message msg) {
        messageListeners.forEach(l -> l.onMessage(msg));
    }

    public void onEvent(RoomEvent event) {
        eventListeners.forEach(l -> l.onEvent(event));
    }

    public void onMessage(JitterMessageListener listener) {
        messageListeners.add(listener);
    }

    public void onEvent(JitterEventListener listener) {
        eventListeners.add(listener);
    }

    public User getCurrentUser() {
        try {
            JSONArray arr = requests().get("/user").asJson().getBody().getArray();
            return new UserImpl(this, arr.getJSONObject(0));
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public Room getRoom(String id) {
        try {
            JSONObject o = requests().get("/rooms/" + id).asJson().getBody().getObject();
            return new RoomImpl(this, o);
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

}
