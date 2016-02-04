package xyz.nickr.jitter.api.listener;

import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.event.RoomActivityEvent;
import xyz.nickr.jitter.api.event.RoomEvent;
import xyz.nickr.jitter.api.event.RoomMentionEvent;
import xyz.nickr.jitter.api.event.RoomUnreadEvent;
import xyz.nickr.jitter.api.event.UserJoinEvent;
import xyz.nickr.jitter.api.event.UserLeaveEvent;
import xyz.nickr.jitter.api.event.UserPresenceEvent;

public interface JitterListener extends JitterMessageListener, JitterRoomEventListener, JitterRoomActivityListener,
                                        JitterUserJoinListener, JitterUserLeaveListener, JitterUserPresenceListener,
                                        JitterRoomUnreadListener, JitterRoomMentionListener {

    @Override
    default void onMessage(Message message) {}

    @Override
    default void onEvent(RoomEvent event) {}

    @Override
    default void onActivity(RoomActivityEvent event) {}

    @Override
    default void onUnread(RoomUnreadEvent event) {}

    @Override
    default void onMention(RoomMentionEvent event) {}

    @Override
    default void onJoin(UserJoinEvent event) {}

    @Override
    default void onLeave(UserLeaveEvent event) {}

    @Override
    default void onUserPresence(UserPresenceEvent event) {}

}
