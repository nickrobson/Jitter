package xyz.nickr.jitter.impl.event;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.event.UserLeaveEvent;

public class UserLeaveEventImpl implements UserLeaveEvent {

    private final Jitter jitter;
    private final Room room;
    private final String userId;
    private final JSONObject json;

    public UserLeaveEventImpl(Jitter jitter, Room room, String userId, JSONObject json) {
        this.jitter = jitter;
        this.room = room;
        this.userId = userId;
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
    public String getUserID() {
        return userId;
    }

}
