package xyz.nickr.jitter;

public class JitterTest {

    public static void main(String[] args) {
        Jitter jitter = Jitter.builder().token(System.getenv("GITTER_TOKEN")).build();

        jitter.onMessage(m -> System.out.println(m.asJSON()));

        jitter.bayeux().subscribeRoom("56b06b13e610378809bf71e7");
        jitter.bayeux().subscribeRoomUsers("56b06b13e610378809bf71e7");
        jitter.bayeux().subscribeRoomEvents("56b06b13e610378809bf71e7");
        jitter.bayeux().subscribeRoomMessages("56b06b13e610378809bf71e7");
    }

}
