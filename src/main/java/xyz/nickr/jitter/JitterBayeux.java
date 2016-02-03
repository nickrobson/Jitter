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

    public static final MessageListener LISTENER = (ch, msg) -> {
        System.out.println("Received Object:");
        System.out.println(new JSONObject(msg).toString());
    };

    public void subscribe(ChannelId chan) {
        ClientSessionChannel channel = bayeux.getChannel(chan);
        channel.subscribe(LISTENER);
        System.out.println("subscribed to " + chan);
    }

    public void subscribeUserRooms(String userId) {
        subscribe(resolve(USER_ROOMS, mapOf(new String[][]{ {"userId", userId} })));
    }

    public void subscribeUserRoomUnread(String userId, String roomId) {
        subscribe(resolve(USER_ROOM_UNREAD, mapOf(new String[][]{ {"userId", userId}, {"roomId", roomId} })));
    }

    public void subscribeUserInformation(String userId) {
        subscribe(resolve(USER_INFORMATION, mapOf(new String[][]{ {"userId", userId} })));
    }

    public void subscribeRoom(String roomId) {
        subscribe(resolve(ROOM, mapOf(new String[][]{ {"roomId", roomId} })));
    }

    public void subscribeRoomUsers(String roomId) {
        subscribe(resolve(ROOM_USERS, mapOf(new String[][]{ {"roomId", roomId} })));
    }

    public void subscribeRoomEvents(String roomId) {
        subscribe(resolve(ROOM_EVENTS, mapOf(new String[][]{ {"roomId", roomId} })));
    }

    public void subscribeRoomMessages(String roomId) {
        subscribe(resolve(ROOM_MESSAGES, mapOf(new String[][]{ {"roomId", roomId} })));
    }

    public void subscribeRoomMessageReadBy(String roomId, String messageId) {
        subscribe(resolve(ROOM_MESSAGES_READ_BY, mapOf(new String[][]{ {"roomId", roomId}, {"messageId", messageId} })));
    }

    Map<String, String> mapOf(String[][] pairs) {
        Map<String, String> map = new HashMap<>();
        for (String[] pair : Objects.requireNonNull(pairs, "pairs"))
            if (pair != null && pair.length == 2)
                map.put(pair[0], pair[1]);
        return map;
    }

}
