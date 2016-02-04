package xyz.nickr.jitter;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.cometd.bayeux.ChannelId;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.client.ClientSessionChannel.MessageListener;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.User;
import xyz.nickr.jitter.impl.MessageImpl;
import xyz.nickr.jitter.impl.RoomImpl;
import xyz.nickr.jitter.impl.UserImpl;
import xyz.nickr.jitter.impl.event.RoomActivityEventImpl;
import xyz.nickr.jitter.impl.event.RoomEventImpl;
import xyz.nickr.jitter.impl.event.RoomMentionEventImpl;
import xyz.nickr.jitter.impl.event.RoomUnreadEventImpl;
import xyz.nickr.jitter.impl.event.UserJoinEventImpl;
import xyz.nickr.jitter.impl.event.UserLeaveEventImpl;
import xyz.nickr.jitter.impl.event.UserPresenceEventImpl;

public class JitterBayeux {

    public static final String FAYE_ENDPOINT = "https://ws.gitter.im/faye";

    public static final String USER_ROOMS = "/api/v1/user/{userId}/rooms";
    public static final String USER_ROOM_UNREAD = "/api/v1/user/{userId}/rooms/{roomId}/unreadItems";
    public static final String USER_INFORMATION = "/api/v1/user/{userId}";
    public static final String ROOM = "/api/v1/rooms/{roomId}";
    public static final String ROOM_USERS = "/api/v1/rooms/{roomId}/users";
    public static final String ROOM_EVENTS = "/api/v1/rooms/{roomId}/events";
    public static final String ROOM_MESSAGES = "/api/v1/rooms/{roomId}/chatMessages";
    public static final String ROOM_MESSAGES_READ_BY = "/api/v1/rooms/{roomId}/chatMessages/{messageId}/readBy";

    private final Jitter jitter;
    private final HttpClient http;
    private final BayeuxClient bayeux;

    JitterBayeux(Jitter jitter) {
        this.jitter = jitter;
        try {

            Map<String, Object> options = new HashMap<>();

            this.http = new HttpClient(new SslContextFactory());
            this.http.setConnectTimeout(5000);
            this.http.setIdleTimeout(0);
            this.http.start();

            LongPollingTransport lp = new LongPollingTransport(options, this.http) {

                @Override
                public void customize(Request request) {
                    StringBuilder json = new StringBuilder();
                    request.getContent().forEach(b -> json.append(new String(b.array(), StandardCharsets.UTF_8)));
                    JSONArray array = new JSONArray(json.toString());
                    JSONObject tokenObject = new JSONObject();
                    tokenObject.put("token", jitter.token);
                    for (int i = 0, j = array.length(); i < j; i++)
                        array.getJSONObject(i).put("ext", tokenObject);
                    request.content(new StringContentProvider(array.toString()));
                }

            };

            this.bayeux = new BayeuxClient(FAYE_ENDPOINT, lp);
            Logger.getLogger(BayeuxClient.class).setLevel(Level.ALL);
            this.bayeux.handshake();
            this.bayeux.waitFor(1000, BayeuxClient.State.CONNECTED);
        } catch (Exception e) {
            e.printStackTrace();
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        }
    }

    public Jitter getJitter() {
        return jitter;
    }

    public HttpClient getHttpClient() {
        return http;
    }

    public BayeuxClient getBayeuxClient() {
        return bayeux;
    }

    public void disconnect() {
        this.bayeux.disconnect();
        this.bayeux.waitFor(1000, BayeuxClient.State.DISCONNECTED);
    }

    public ChannelId resolve(String path, Map<String, String> vars) {
        for (Entry<String, String> e : vars.entrySet()) {
            path = path.replace("{" + e.getKey() + "}", e.getValue());
        }
        return new ChannelId(path);
    }

    public void subscribe(ChannelId chan, MessageListener listener) {
        ClientSessionChannel channel = bayeux.getChannel(chan);
        channel.subscribe(listener);
    }

    public void subscribeUserRooms(final User user) {
        subscribe(resolve(USER_ROOMS, mapOf(new String[][]{ {"userId", user.getID()} })), (ch, msg) -> {
            JSONObject json = new JSONObject(msg);
            JSONObject data = json.getJSONObject("data");
            JSONObject model = data.optJSONObject("model");
            String op = data.getString("operation");
            if (op.equals("create")) {
                jitter.rooms.put(model.getString("id"), new RoomImpl(jitter, model));
            } else if (op.equals("patch")) {
                Room room = jitter.rooms.get(model.getString("id"));
                room.update(model);
            } else if (op.equals("remove")) {
                // removed
            }
        });
    }

    public void subscribeUserRoomUnread(final User user, final Room room) {
        subscribe(resolve(USER_ROOM_UNREAD, mapOf(new String[][]{ {"userId", user.getID()}, {"roomId", room.getID()} })), (ch, msg) -> {
            System.out.println("User room unread: " + new JSONObject(msg));
        });
    }

    public void subscribeUserInformation(final User user) {
        subscribe(resolve(USER_INFORMATION, mapOf(new String[][]{ {"userId", user.getID()} })), (ch, msg) -> {
            JSONObject json = new JSONObject(msg);
            JSONObject data = json.getJSONObject("data");
            String notif = data.getString("notification");
            String roomId = data.getString("troupeId");
            Room room = jitter.getRoom(roomId);
            if (notif.equals("activity")) {
                jitter.onActivity(new RoomActivityEventImpl(jitter, room, json));
            } else if (notif.equals("user_notification")) {

            } else if (notif.equals("troupe_unread")) {
                jitter.onUnread(new RoomUnreadEventImpl(jitter, room, data.getInt("totalUnreadItems"), json));
            } else if (notif.equals("troupe_mention")) {
                jitter.onMention(new RoomMentionEventImpl(jitter, room, data.getInt("mentions"), json));
            } else {
                System.out.println("User information: " + new JSONObject(msg));
            }
        });
    }

    public void subscribeRoom(final Room room) {
        subscribe(resolve(ROOM, mapOf(new String[][]{ {"roomId", room.getID()} })), (ch, msg) -> {
            jitter.onPresence(new UserPresenceEventImpl(jitter, room, new JSONObject(msg)));
        });
    }

    public void subscribeRoomUsers(final Room room) {
        subscribe(resolve(ROOM_USERS, mapOf(new String[][]{ {"roomId", room.getID()} })), (ch, msg) -> {
            JSONObject json = new JSONObject(msg);
            String op = json.getJSONObject("data").getString("operation");
            if (op.equals("create")) {
                jitter.onJoin(new UserJoinEventImpl(jitter, room, new UserImpl(jitter, json.getJSONObject("data").getJSONObject("model")), json));
            } else if (op.equals("remove")) {
                jitter.onLeave(new UserLeaveEventImpl(jitter, room, json.getJSONObject("data").getJSONObject("model").getString("id"), json));
            }
        });
    }

    public void subscribeRoomEvents(final Room room) {
        subscribe(resolve(ROOM_EVENTS, mapOf(new String[][]{ {"roomId", room.getID()} })), (ch, msg) -> {
            jitter.onEvent(new RoomEventImpl(jitter, room, new JSONObject(msg).getJSONObject("data").getJSONObject("model")));
        });
    }

    public void subscribeRoomMessages(final Room room) {
        subscribe(resolve(ROOM_MESSAGES, mapOf(new String[][]{ {"roomId", room.getID()} })), (ch, msg) -> {
            JSONObject data = new JSONObject(msg).getJSONObject("data");
            String op = data.getString("operation");
            System.out.println(msg);
            System.out.println(data);
            System.out.println(data.getJSONObject("model"));
            if (op.equals("create")) {
                jitter.onMessage(new MessageImpl(jitter, room, data.getJSONObject("model")));
            } else if (op.equals("patch")) {

            }
        });
    }

    public void subscribeRoomMessageReadBy(final Message message) {
        subscribe(resolve(ROOM_MESSAGES_READ_BY, mapOf(new String[][]{ {"roomId", message.getRoom().getID()}, {"messageId", message.getID()} })), (ch, msg) -> {
            System.out.println("Message read by: " + msg);
        });
    }

    Map<String, String> mapOf(String[][] pairs) {
        Map<String, String> map = new HashMap<>();
        for (String[] pair : Objects.requireNonNull(pairs, "pairs"))
            if (pair != null && pair.length == 2)
                map.put(pair[0], pair[1]);
        return map;
    }

}
