package xyz.nickr.jitter.impl.event;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.event.RoomActivityEvent;

public class RoomActivityEventImpl implements RoomActivityEvent {

    private final Jitter jitter;
    private final Room room;
    private final JSONObject json;

    public RoomActivityEventImpl(Jitter jitter, Room room, JSONObject json) {
        this.jitter = jitter;
        this.room = room;
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

}
