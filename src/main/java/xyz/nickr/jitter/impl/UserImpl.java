package xyz.nickr.jitter.impl;

import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.User;

public class UserImpl implements User {

    protected final Jitter jitter;
    protected final JSONObject json;

    public UserImpl(Jitter jitter, JSONObject json) {
        this.jitter = jitter;
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
    public String getID() {
        return json.getString("id");
    }

    @Override
    public String getUsername() {
        return json.getString("username");
    }

    @Override
    public String getDisplayName() {
        return json.getString("displayName");
    }

    @Override
    public String getURL() {
        return json.getString("url");
    }

    @Override
    public String getSmallAvatarURL() {
        return json.getString("avatarUrlSmall");
    }

    @Override
    public String getMediumAvatarURL() {
        return json.getString("avatarUrlMedium");
    }

    @Override
    public String toString() {
        return json.toString();
    }

    @Override
    public Message sendMessage(String message) {
        try {
            JSONObject req = new JSONObject();
            req.put("uri", getUsername());
            JSONObject resp = jitter.requests().post("/rooms").body(new JsonNode(req.toString())).asJson().getBody().getObject();
            Room room = new RoomImpl(jitter, resp);
            return room.sendMessage(message);
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

}
