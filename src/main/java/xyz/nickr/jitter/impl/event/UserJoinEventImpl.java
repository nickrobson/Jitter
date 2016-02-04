package xyz.nickr.jitter.impl.event;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.User;
import xyz.nickr.jitter.api.event.UserJoinEvent;

public class UserJoinEventImpl implements UserJoinEvent {

    private final Jitter jitter;
    private final Room room;
    private final User user;
    private final JSONObject json;

    public UserJoinEventImpl(Jitter jitter, Room room, User user, JSONObject json) {
        this.jitter = jitter;
        this.room = room;
        this.user = user;
        this.json = json;
    }

    @Override
    public Jitter getJitter() {
        return jitter;
    }

    @Override
    public JSONObject asJSON() {
        return json;
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public User getUser() {
        return user;
    }

}
