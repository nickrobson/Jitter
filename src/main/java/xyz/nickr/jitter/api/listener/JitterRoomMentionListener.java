package xyz.nickr.jitter.api.listener;

import xyz.nickr.jitter.api.event.RoomMentionEvent;

/**
 * Represents a listener for when a room has a changing amount of mentions.
 *
 * @author Nick Robson
 */
public interface JitterRoomMentionListener {

    /**
     * Called when a room has a changing amount of mentions.
     *
     * @param event The event.
     */
    void onMention(RoomMentionEvent event);

}