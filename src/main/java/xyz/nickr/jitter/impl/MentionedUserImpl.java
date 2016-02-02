package xyz.nickr.jitter.impl;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.MentionedUser;
import xyz.nickr.jitter.api.Message;

public class MentionedUserImpl implements MentionedUser {

    private final Jitter jitter;
    private final MessageImpl message;
    private final JSONObject json;

    public MentionedUserImpl(Jitter jitter, MessageImpl message, JSONObject json) {
        this.jitter = jitter;
        this.message = message;
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
    public Message getMessage() {
        return message;
    }

    @Override
    public String getID() {
        return json.getString("userId");
    }

    @Override
    public String getDisplayName() {
        return json.getString("screenName");
    }

    @Override
    public String toString() {
        return json.toString();
    }

}
