package xyz.nickr.jitter.impl;

import java.util.Date;
import java.util.Optional;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.RoomEvent;
import xyz.nickr.jitter.api.User;

public class RoomEventImpl implements RoomEvent {

    private final Jitter jitter;
    private final Room room;
    private final JSONObject json;

    public RoomEventImpl(Jitter jitter, Room room, JSONObject json) {
        this.jitter = jitter;
        this.room = room;
        this.json = json;
    }

    @Override
    public Jitter getJitter() {
        return jitter;
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public JSONObject asJSON() {
        return json;
    }

    @Override
    public String getID() {
        return json.getString("id");
    }

    @Override
    public String getService() {
        try {
            return getMetadata().getString("service");
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getAction() {
        try {
            return getMetadata().getString("action");
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getText() {
        return json.getString("text");
    }

    @Override
    public String getHtml() {
        return json.getString("html");
    }

    @Override
    public Date getSentTimestamp() {
        return javax.xml.bind.DatatypeConverter.parseDateTime(json.getString("sent")).getTime();
    }

    @Override
    public Optional<Date> getEditTimestamp() {
        return json.has("editedAt") ? Optional.of(javax.xml.bind.DatatypeConverter.parseDateTime(json.optString("editedAt")).getTime()) : Optional.empty();
    }

    @Override
    public Optional<User> getUser() {
        return json.opt("fromUser") != null ? Optional.of(new UserImpl(jitter, json.getJSONObject("fromUser"))) : Optional.empty();
    }

    @Override
    public JSONObject getMetadata() {
        return json.getJSONObject("meta");
    }

    @Override
    public JSONObject getPayload() {
        return json.getJSONObject("payload");
    }

    @Override
    public int getVersion() {
        return json.getInt("v");
    }

}
