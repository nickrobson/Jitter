package xyz.nickr.jitter.api;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

import xyz.nickr.jitter.Jitter;

public interface Message {

    Jitter getJitter();

    JSONObject asJSON();

    Room getRoom();

    User getSender();

    String getID();

    String getText();

    String getHtml();

    Date getSentTimestamp();

    Optional<Date> getEditTimestamp();

    void edit(String message);

    boolean isRead();

    int getReadCount();

    List<String> getURLs();

    List<MentionedUser> getMentions();

    List<MentionedIssue> getIssues();

    MessageMetadata getMetadata();

    int getVersion();

}
