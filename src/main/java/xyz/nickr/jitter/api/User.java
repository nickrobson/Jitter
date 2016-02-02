package xyz.nickr.jitter.api;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;

public interface User {

    Jitter getJitter();

    JSONObject asJSON();

    String getID();

    String getUsername();

    String getDisplayName();

    String getURL();

    String getSmallAvatarURL();

    String getMediumAvatarURL();

    Message sendMessage(String message);

}
