package xyz.nickr.jitter.impl;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;

import com.mashape.unirest.http.exceptions.UnirestException;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.MessageHistory;

public class MessageHistoryImpl implements MessageHistory {

    private RoomImpl room;
    private List<Message> messages;

    public MessageHistoryImpl(RoomImpl room) {
        this.room = room;

        loadMessages(50);
    }

    public MessageHistoryImpl(RoomImpl room, List<Message> messages) {
        this.room = room;
        this.messages = messages;
    }

    @Override
    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public List<Message> loadMessages(int n) {
        try {
            String params = "?limit=" + n;
            if (messages != null) {
                params += "&beforeId=" + messages.get(0).getID();
            }
            Jitter jitter = room.getJitter();
            List<Message> messages = new LinkedList<>();
            JSONArray arr = jitter.requests().get("/rooms/" + room.getID() + "/chatMessages" + params).asJson().getBody().getArray();
            if (arr.length() == 0)
                return messages;
            for (int i = 0, j = arr.length(); i < j; i++) {
                messages.add(new MessageImpl(jitter, room, arr.getJSONObject(i)));
            }
            if (this.messages == null) {
                this.messages = messages;
            } else {
                this.messages.addAll(0, messages);
            }
            return messages;
        } catch (UnirestException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Message> fullyLoad() {
        List<Message> sofar = new LinkedList<>();
        int size;
        do {
            size = sofar.size();
            List<Message> n = loadMessages(50);
            if (n == null || n.isEmpty())
                break;
            sofar.addAll(0, n);
        } while (size < sofar.size());
        return sofar;
    }

    @Override
    public MessageHistory before(Date date) {
        for (int i = messages.size(); i > 0; i--) {
            if (messages.get(i-1).getSentTimestamp().before(date)) {
                return new MessageHistoryImpl(room, messages.subList(0, i-1));
            }
        }
        return new MessageHistoryImpl(room, Collections.emptyList());
    }

    @Override
    public MessageHistory after(Date date) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getSentTimestamp().after(date)) {
                return new MessageHistoryImpl(room, messages.subList(i, messages.size()));
            }
        }
        return new MessageHistoryImpl(room, Collections.emptyList());
    }

}
