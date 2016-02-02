package xyz.nickr.jitter.impl;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.RoomUser;

public class RoomUserImpl extends UserImpl implements RoomUser {

    protected RoomImpl room;

    public RoomUserImpl(Jitter jitter, RoomImpl room, JSONObject json) {
        super(jitter, json);
        this.room = room;
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public boolean isAdmin() {
        return json.has("role") && json.getString("role").equals("admin");
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject(json.toString());
        obj.put("roomId", room.getID());
        return obj.toString();
    }

}
