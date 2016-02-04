package xyz.nickr.jitter.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.MessageHistory;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.RoomType;
import xyz.nickr.jitter.api.RoomUser;

public class RoomImpl implements Room {

    private final Jitter jitter;
    private final JSONObject json;

    public RoomImpl(Jitter jitter, JSONObject json) {
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
    @SuppressWarnings("unchecked")
    public void update(JSONObject data) {
        Stream<String> stream = data.keySet().stream().map(s -> s.toString());
        Set<String> keys = stream.collect(Collectors.toSet());
        for (String s : keys) {
            json.put(s, data.get(s));
        }
        if (keys.isEmpty())
            json.remove("favourite");
    }

    @Override
    public String getID() {
        return json.getString("id");
    }

    @Override
    public String getName() {
        return json.getString("name");
    }

    @Override
    public String getTopic() {
        return json.getString("topic");
    }

    @Override
    public RoomType getType() {
        switch (json.getString("githubType")) {
            case "ONETOONE":
                return RoomType.USER;
            case "REPO":
                return RoomType.REPOSITORY;
            case "ORG":
                return RoomType.ORGANISATION;
            case "USER_CHANNEL":
                return RoomType.USER_CHANNEL;
            case "REPO_CHANNEL":
                return RoomType.REPOSITORY_CHANNEL;
            case "ORG_CHANNEL":
                return RoomType.ORGANISATION_CHANNEL;
        }
        return RoomType.UNKNOWN;
    }

    @Override
    public String getURI() {
        return json.optString("uri", json.getString("url"));
    }

    @Override
    public int getUserCount() {
        return json.getInt("userCount");
    }

    @Override
    public int getUnreadItems() {
        return json.getInt("unreadItems");
    }

    @Override
    public int getUnreadMentions() {
        return json.getInt("mentions");
    }

    @Override
    public String getLastAccessTime() {
        return json.optString("lastAccessTime");
    }

    @Override
    public boolean isFavourite() {
        return json.has("favourite") && json.getInt("favourite") > 0;
    }

    @Override
    public boolean isLurkEnabled() {
        return json.getBoolean("lurk");
    }

    @Override
    public String getPathURL() {
        return json.getString("url");
    }

    @Override
    public List<String> getTags() {
        List<String> tags = new LinkedList<>();
        JSONArray arr = json.getJSONArray("tags");
        for (int i = 0; i < arr.length(); i++)
            tags.add(arr.getString(i));
        return tags;
    }

    @Override
    public int getVersion() {
        return json.optInt("v", 0);
    }

    @Override
    public List<Room> getChannels() {
        List<Room> chans = new LinkedList<>();
        try {
            JSONArray arr = jitter.requests().get("/rooms/" + getID() + "/channels").asJson().getBody().getArray();
            for (int i = 0, j = arr.length(); i < j; i++) {
                chans.add(new RoomImpl(jitter, arr.getJSONObject(i)));
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return chans;
    }

    @Override
    public List<RoomUser> getUsers() {
        List<RoomUser> users = new LinkedList<>();
        if (json.has("user")) {
            users.add(new RoomUserImpl(jitter, this, json.getJSONObject("user")));
        } else {
            try {
                JSONArray arr = jitter.requests().get("/rooms/" + getID() + "/users").asJson().getBody().getArray();
                for (int i = 0, j = arr.length(); i < j; i++) {
                    users.add(new RoomUserImpl(jitter, this, arr.getJSONObject(i)));
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    @Override
    public Message getMessage(String id) {
        try {
            JSONObject json = Unirest.get("/rooms/" + getID() + "/chatMessages/" + id).asJson().getBody().getObject();
            return new MessageImpl(jitter, this, json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MessageHistory getMessageHistory() {
        return new MessageHistoryImpl(this);
    }

    @Override
    public MessageHistory getMessagesBefore(Message message) {
        return new MessageHistoryImpl(this, message, true);
    }

    @Override
    public MessageHistory getMessagesAfter(Message message) {
        return new MessageHistoryImpl(this, message, false);
    }

    @Override
    public Message sendMessage(String message) {
        JSONObject json = new JSONObject();
        json.put("text", message);
        try {
            JSONObject res = jitter.requests().post("/rooms/" + getID() + "/chatMessages")
                                        .body(new JsonNode(json.toString()))
                                        .asJson().getBody().getObject();
            return new MessageImpl(jitter, this, res);
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return json.toString();
    }

}
