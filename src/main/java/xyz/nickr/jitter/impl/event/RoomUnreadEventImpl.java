package xyz.nickr.jitter.impl.event;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.event.RoomUnreadEvent;

public class RoomUnreadEventImpl implements RoomUnreadEvent {

    private final Jitter jitter;
    private final Room room;
    private final int unread;
    private final JSONObject json;

    public RoomUnreadEventImpl(Jitter jitter, Room room, int unread, JSONObject json) {
        this.jitter = jitter;
        this.room = room;
        this.unread = unread;
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
    public int getUnreadMessages() {
        return unread;
    }

}
