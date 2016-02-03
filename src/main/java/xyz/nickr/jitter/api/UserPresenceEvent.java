package xyz.nickr.jitter.api;

import xyz.nickr.jitter.Jitter;

public interface UserPresenceEvent {

    Jitter getJitter();

    Room getRoom();

    String getUserID();

    boolean isIncoming();

}
