package xyz.nickr.jitter.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import xyz.nickr.jitter.Jitter;
import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.MessageHistory;
import xyz.nickr.jitter.api.Room;

public class MessageHistoryImpl implements MessageHistory {

    private Room room;
    private List<Message> messages;
    private boolean loadBefore = true;

    public MessageHistoryImpl(Room room) {
        this.room = room;
        loadMessages(50);
    }

    public MessageHistoryImpl(Room room, List<Message> messages) {
        this.room = room;
        this.messages = messages;
    }

    public MessageHistoryImpl(Room room, Message message, boolean loadBefore) {
        this.room = room;
        this.messages = new LinkedList<>(Arrays.asList(message));
        this.loadBefore = loadBefore;
        loadMessages(50);
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public List<Message> loadMessages(int n) {
        try {
            String params = "?limit=" + n;
            if (messages != null && !messages.isEmpty()) {
                if (loadBefore)
                    params += "&beforeId=" + messages.get(0).getID();
                else
                    params += "&afterId=" + messages.get(messages.size() - 1).getID();
            }
            Jitter jitter = room.getJitter();
            List<Message> messages = new LinkedList<>();
            JSONArray arr = jitter.requests().get("/rooms/" + room.getID() + "/chatMessages" + params).asJson().getBody().getArray();
            if (arr.length() == 0)
                return messages;
            for (int i = 0, j = arr.length(); i < j; i++) {
                messages.add(new MessageImpl(jitter, room, new UserImpl(jitter, arr.getJSONObject(i).getJSONObject("fromUser")), arr.getJSONObject(i)));
            }
            if (this.messages == null) {
                this.messages = messages;
            } else {
                if (loadBefore)
                    this.messages.addAll(0, messages);
                else
                    this.messages.addAll(messages);
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
            List<Message> msgs = loadMessages(50);
            if (msgs == null || msgs.isEmpty())
                break;
            if (loadBefore)
                sofar.addAll(0, msgs);
            else
                sofar.addAll(msgs);
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

    @Override
    public Message getEarliest() {
        return messages.isEmpty() ? null : messages.get(0);
    }

    @Override
    public Message getLatest() {
        return messages.isEmpty() ? null : messages.get(messages.size() - 1);
    }

    @Override
    public void markRead() {
        JSONObject json = new JSONObject();
        JSONArray chat = new JSONArray();
        for (Message message : messages) {
            if (!message.isRead())
                chat.put(message.getID());
            ((MessageImpl) message).setRead(true);
        }
        if (chat.length() == 0)
            return;
        json.put("chat", chat);
        try {
            room.getJitter().requests()
                .post("/user/" + room.getJitter().getCurrentUser().getID() + "/rooms/" + room.getID() + "/unreadItems")
                .body(new JsonNode(json.toString()))
                .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

}
