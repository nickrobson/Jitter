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
import xyz.nickr.jitter.impl.MessageImpl;
import xyz.nickr.jitter.impl.UserImpl;
import xyz.nickr.jitter.impl.event.MessageReceivedEventImpl;
import xyz.nickr.jitter.impl.event.RoomEventImpl;

/**
 * A utility class for managing the Gitter Streaming API on behalf of a Jitter object.
 *
 * @author Nick Robson
 */
public class JitterStream {

    private final Jitter jitter;

    // keep track of rooms that are already listening
    private final Set<String> messageStreaming = new HashSet<>();
    private final Set<String> eventsStreaming = new HashSet<>();

    JitterStream(Jitter jitter) {
        this.jitter = jitter;
    }

    /**
     * The providing {@link Jitter} object.
     *
     * @return The provider.
     */
    public Jitter getJitter() {
        return jitter;
    }

    /**
     * Gets a BufferedReader over the given room's messages stream.
     * <br><br>
     * <b>This is a BLOCKING method!</b> <em>Use {@link #beginMessagesStream(Room)} for non-blocking</em>
     *
     * @param roomId The room ID.
     *
     * @return The BufferedReader.
     *
     * @throws IOException If an error occurs.
     */
    public BufferedReader getMessagesStream(String roomId) throws IOException {
        InputStream res = jitter.requests().stream("/rooms/" + roomId + "/chatMessages");
        return new BufferedReader(new InputStreamReader(res));
    }

    /**
     * Gets a BufferedReader over the given room's events stream.
     * <br><br>
     * <b>This is a BLOCKING method!</b> <em>Use {@link #beginEventsStream(Room)} for non-blocking</em>
     *
     * @param roomId The room ID.
     *
     * @return The BufferedReader.
     *
     * @throws IOException If an error occurs.
     */
    public BufferedReader getEventsStream(String roomId) throws IOException {
        InputStream res = jitter.requests().stream("/rooms/" + roomId + "/events");
        return new BufferedReader(new InputStreamReader(res));
    }

    /**
     * Begins a message stream over a given {@link Room}.
     * <br><br>
     * Messages will be propagated through {@link Jitter}'s event system.
     *
     * @param room The room.
     */
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
                            JSONObject object = new JSONObject(line);
                            Message event = new MessageImpl(jitter, room, new UserImpl(jitter, object.getJSONObject("fromUser")), object);
                            jitter.events().on(new MessageReceivedEventImpl(event));
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

    /**
     * Begins an event stream over a given {@link Room}.
     * <br><br>
     * Events will be propagated through {@link Jitter}'s event system.
     *
     * @param room The room.
     */
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
                            jitter.events().on(new RoomEventImpl(jitter, room, new JSONObject(line)));
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
