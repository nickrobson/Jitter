package xyz.nickr.jitter.impl;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.MentionedIssue;
import xyz.nickr.jitter.api.Message;

public class MentionedIssueImpl implements MentionedIssue {

    private final Jitter jitter;
    private final MessageImpl message;
    private final JSONObject json;

    public MentionedIssueImpl(Jitter jitter, MessageImpl message, JSONObject json) {
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
    public int getNumber() {
        return json.getInt("number");
    }

    @Override
    public String toString() {
        return json.toString();
    }

}
