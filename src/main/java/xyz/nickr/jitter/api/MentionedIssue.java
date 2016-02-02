package xyz.nickr.jitter.api;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;

public interface MentionedIssue {

    Jitter getJitter();

    JSONObject asJSON();

    Message getMessage();

    int getNumber();

}
