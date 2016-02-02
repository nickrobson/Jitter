package xyz.nickr.jitter.api;

public interface RoomUser extends User {

    Room getRoom();

    boolean isAdmin();

}
