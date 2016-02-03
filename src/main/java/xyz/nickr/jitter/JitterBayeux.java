package xyz.nickr.jitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.cometd.bayeux.ChannelId;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.client.ClientSessionChannel.MessageListener;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;

public class JitterBayeux {

    public static final String FAYE_ENDPOINT = "https://ws.gitter.im/faye";

    public static final String USER_ROOMS = "/api/v1/user/{userId}/rooms";
    public static final String USER_ROOM_UNREAD = "/api/v1/user/{userId}/rooms/{roomId}/unreadItems";
    public static final String USER_INFORMATION = "/api/v1/user/{userId}";
    public static final String ROOM_USER_PRESENCE = "/api/v1/rooms/{roomId}";
    public static final String ROOM_USERS = "/api/v1/rooms/{roomId}/users";
    public static final String ROOM_EVENTS = "/api/v1/rooms/{roomId}/events";
    public static final String ROOM_MESSAGES = "/api/v1/rooms/{roomId}/chatMessages";
    public static final String ROOM_MESSAGES_READ_BY = "/api/v1/rooms/{roomId}/chatMessages/{messageId}/readBy";

    private static final MessageListener CHANNEL_LISTENER = (ch, msg) -> {
        System.out.println("Received Object: " + msg);
        msg.entrySet().forEach(e -> System.out.println(e));
    };

    private final Jitter jitter;
    private final HttpClient http;
    private final BayeuxClient bayeux;

    JitterBayeux(Jitter jitter) {
        this.jitter = jitter;
        try {
            this.http = new HttpClient();
            this.http.setConnectTimeout(5000);
            this.http.start();

            Map<String, Object> options = new HashMap<>();
            LongPollingTransport transport = new LongPollingTransport(options, http) {

                @Override
                public void customize(Request request) {
                    request.header(HttpHeader.AUTHORIZATION, jitter.token);
                }

            };
            this.bayeux = new BayeuxClient(FAYE_ENDPOINT, transport);
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

    public void subscribe(ChannelId chan) {
        ClientSessionChannel channel = bayeux.getChannel(chan);
        channel.subscribe(CHANNEL_LISTENER);
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

    public void subscribeRoomUserPresence(String roomId) {
        subscribe(resolve(ROOM_USER_PRESENCE, mapOf(new String[][]{ {"roomId", roomId} })));
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
