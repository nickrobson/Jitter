package xyz.nickr.jitter.api;

import java.util.Date;
import java.util.List;

public interface MessageHistory {

    List<Message> getMessages();

    List<Message> loadMessages(int n);

    List<Message> fullyLoad();

    MessageHistory before(Date date);

    MessageHistory after(Date date);

}
