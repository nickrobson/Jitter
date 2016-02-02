package xyz.nickr.jitter.api;

import java.util.Date;
import java.util.Optional;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;

public interface RoomEvent {

    Jitter getJitter();

    JSONObject asJSON();

    String getID();

    String getService();

    String getAction();

    String getText();

    String getHtml();

    Room getRoom();

    Date getSentTimestamp();

    Optional<Date> getEditTimestamp();

    Optional<User> getUser();

    JSONObject getMetadata();

    JSONObject getPayload();

    int getVersion();

}
