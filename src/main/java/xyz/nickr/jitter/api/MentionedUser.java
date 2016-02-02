package xyz.nickr.jitter.api;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;

public interface MentionedUser {

    Jitter getJitter();

    JSONObject asJSON();

    Message getMessage();

    String getID();

    String getDisplayName();

}
