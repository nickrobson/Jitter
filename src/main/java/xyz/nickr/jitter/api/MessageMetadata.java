package xyz.nickr.jitter.api;

import org.json.JSONArray;

import xyz.nickr.jitter.Jitter;

public interface MessageMetadata {

    Jitter getJitter();

    Message getMessage();

    JSONArray asJSON();

}
