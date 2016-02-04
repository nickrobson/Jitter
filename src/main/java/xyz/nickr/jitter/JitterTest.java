package xyz.nickr.jitter;

import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.api.User;

public class JitterTest {

    public static void main(String[] args) {
        Jitter jitter = Jitter.builder().token(System.getenv("GITTER_TOKEN")).build();

        jitter.onMessage(m -> System.out.println("Message: " + m.asJSON()));
        jitter.onEvent(e -> System.out.println("Event: " + e.asJSON()));
        jitter.onPresence(e -> System.out.println("Presence: " + e.getUserID() + (e.isIncoming() ? " is now" : " stopped") + " watching " + e.getRoom().getName()));
        jitter.onJoin(e -> System.out.println("Join: " + e.getUser().getUsername() + " in " + e.getRoom().getName()));
        jitter.onLeave(e -> System.out.println("Leave: " + e.getUserID() + " in " + e.getRoom().getName()));

        User user = jitter.getCurrentUser();
        Room room = jitter.getRoom("56b06b13e610378809bf71e7");

        room.sendMessage("hey");

        jitter.bayeux().subscribeUserRooms(user);
        jitter.bayeux().subscribeUserRoomUnread(user, room);
        jitter.bayeux().subscribeUserInformation(user);

        jitter.bayeux().subscribeRoom(room);
        jitter.bayeux().subscribeRoomUsers(room);
        jitter.bayeux().subscribeRoomEvents(room);
        jitter.bayeux().subscribeRoomMessages(room);
    }

}
