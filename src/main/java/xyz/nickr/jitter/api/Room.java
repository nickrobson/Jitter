package xyz.nickr.jitter.api;

import java.util.List;
import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;

public interface Room {

    Jitter getJitter();

    JSONObject asJSON();

    String getID();

    String getName();

    String getTopic();

    RoomType getType();

    String getURI();

    int getUserCount();

    int getUnreadItems();

    int getUnreadMentions();

    String getLastAccessTime();

    boolean isFavourite();

    boolean isLurkEnabled();

    String getPathURL();

    List<String> getTags();

    int getVersion();

    List<Room> getChannels();

    List<RoomUser> getUsers();

    MessageHistory getMessageHistory();

    Message sendMessage(String message);

    default void beginMessageStreaming() {
        getJitter().stream().beginMessagesStream(this);
    }

    default void beginEventsStreaming() {
        getJitter().stream().beginEventsStream(this);
    }

}
