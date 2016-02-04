package xyz.nickr.jitter.impl.event;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.event.RoomMentionEvent;

public class RoomMentionEventImpl implements RoomMentionEvent {

    private final Jitter jitter;
    private final Room room;
    private final int mentions;
    private final JSONObject json;

    public RoomMentionEventImpl(Jitter jitter, Room room, int mentions, JSONObject json) {
        this.jitter = jitter;
        this.room = room;
        this.mentions = mentions;
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
    public int getMentions() {
        return mentions;
    }

}
