package xyz.nickr.jitter.impl.event;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.event.RoomReadMessagesEvent;

public class RoomReadMessagesEventImpl implements RoomReadMessagesEvent {

    private final Jitter jitter;
    private final Room room;
    private final String[] ids;
    private final boolean unread;
    private final JSONObject json;

    public RoomReadMessagesEventImpl(Jitter jitter, Room room, String[] ids, boolean unread, JSONObject json) {
        this.jitter = jitter;
        this.room = room;
        this.ids = ids;
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
    public String[] getMessageIds() {
        return ids;
    }

    @Override
    public boolean isUnread() {
        return unread;
    }

    @Override
    public Room getRoom() {
        return room;
    }

}
