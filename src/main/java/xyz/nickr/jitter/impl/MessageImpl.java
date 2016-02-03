package xyz.nickr.jitter.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.MentionedIssue;
import xyz.nickr.jitter.api.MentionedUser;
import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.MessageMetadata;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.User;

public class MessageImpl implements Message {

    private final Jitter jitter;
    private final JSONObject json;

    private final Room room;
    private final User user;

    private final List<String> urls;
    private final List<MentionedUser> mentions;
    private final List<MentionedIssue> issues;

    private final MessageMetadata metadata;

    public MessageImpl(Jitter jitter, Room room, JSONObject json) {
        this.jitter = jitter;
        this.json = json;
        this.room = room;
        this.user = new UserImpl(jitter, json.getJSONObject("fromUser"));

        this.urls = new LinkedList<>();
        JSONArray urls = json.getJSONArray("urls");
        for (int i = 0; i < urls.length(); i++) {
            JSONObject o = urls.optJSONObject(i);
            if (o != null && o.has("url"))
                this.urls.add(o.getString("url"));
        }

        this.mentions = new LinkedList<>();
        JSONArray mentions = json.getJSONArray("urls");
        for (int i = 0; i < mentions.length(); i++) {
            JSONObject o = mentions.optJSONObject(i);
            if (o != null)
                this.mentions.add(new MentionedUserImpl(jitter, this, o));
        }

        this.issues = new LinkedList<>();
        JSONArray issues = json.getJSONArray("urls");
        for (int i = 0; i < issues.length(); i++) {
            JSONObject o = issues.optJSONObject(i);
            if (o != null)
                this.issues.add(new MentionedIssueImpl(jitter, this, o));
        }

        this.metadata = new MessageMetadataImpl(jitter, this, json.getJSONArray("meta"));
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
    public User getSender() {
        return user;
    }

    @Override
    public String getID() {
        return json.getString("id");
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
    public void edit(String message) {
        HttpRequestWithBody req = jitter.requests().put("/rooms/" + room.getID() + "/chatMessages/" + getID());
        json.put("editedAt", new Date());
        json.put("text", message);
        json.put("html", jitter.toHtml(message));
        req.body(new JsonNode(json.toString()));
        try {
            req.asString(); // submit it
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRead() {
        return !json.getBoolean("unread");
    }

    void setRead(boolean read) {
        json.put("unread", !read);
    }

    @Override
    public int getReadCount() {
        return json.getInt("readBy");
    }

    @Override
    public List<String> getURLs() {
        return urls;
    }

    @Override
    public List<MentionedUser> getMentions() {
        return mentions;
    }

    @Override
    public List<MentionedIssue> getIssues() {
        return issues;
    }

    @Override
    public MessageMetadata getMetadata() {
        return metadata;
    }

    @Override
    public int getVersion() {
        return json.getInt("v");
    }

    @Override
    public String toString() {
        return json.toString();
    }

}
