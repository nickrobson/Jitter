package xyz.nickr.jitter.impl;

import org.json.JSONArray;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.MessageMetadata;

public class MessageMetadataImpl implements MessageMetadata {

    private final Jitter jitter;
    private final MessageImpl message;
    private final JSONArray json;

    public MessageMetadataImpl(Jitter jitter, MessageImpl message, JSONArray json) {
        this.jitter = jitter;
        this.message = message;
        this.json = json;
    }

    @Override
    public Jitter getJitter() {
        return jitter;
    }

    @Override
    public JSONArray asJSON() {
        return json;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return json.toString();
    }

}
