package xyz.nickr.jitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONObject;

import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.RoomEvent;
import xyz.nickr.jitter.impl.MessageImpl;
import xyz.nickr.jitter.impl.RoomEventImpl;

public class JitterStream {

    private final Jitter jitter;

    private final Set<String> messageStreaming = new HashSet<>();
    private final Set<String> eventsStreaming = new HashSet<>();

    JitterStream(Jitter jitter) {
        this.jitter = jitter;
    }

    public Jitter getJitter() {
        return jitter;
    }

    public BufferedReader getMessagesStream(String roomId) throws IOException {
        InputStream res = jitter.requests().stream("/rooms/" + roomId + "/chatMessages");
        return new BufferedReader(new InputStreamReader(res));
    }

    public BufferedReader getEventsStream(String roomId) throws IOException {
        InputStream res = jitter.requests().stream("/rooms/" + roomId + "/events");
        return new BufferedReader(new InputStreamReader(res));
    }

    public void beginMessagesStream(final Room room) {
        final String roomId = room.getID();
        if (messageStreaming.contains(roomId))
            return;
        messageStreaming.add(roomId);
        new Thread(() -> {
            try {
                final BufferedReader reader = getMessagesStream(roomId);
                while (true) {
                    String line = reader.readLine();
                    if (line != null && !(line = line.trim()).isEmpty()) {
                        try {
                            System.out.println(line);
                            JSONObject object = new JSONObject(line);
                            Message event = new MessageImpl(jitter, room, object);
                            jitter.onMessage(event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void beginEventsStream(final Room room) {
        final String roomId = room.getID();
        if (eventsStreaming.contains(roomId))
            return;
        eventsStreaming.add(roomId);
        new Thread(() -> {
            try {
                final BufferedReader reader = getEventsStream(roomId);
                while (true) {
                    String line = reader.readLine();
                    if (line != null && !(line = line.trim()).isEmpty()) {
                        try {
                            JSONObject object = new JSONObject(line);
                            RoomEvent event = new RoomEventImpl(jitter, room, object);
                            jitter.onEvent(event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
