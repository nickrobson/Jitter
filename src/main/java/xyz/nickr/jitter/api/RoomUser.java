package xyz.nickr.jitter.api;

/**
 * Represents a {@link User user} inside a {@link Room room}.
 *
 * @author Nick Robson
 */
public interface RoomUser extends User {

    /**
     * Gets the {@link Room room}.
     *
     * @return The room.
     */
    Room getRoom();

    /**
     * Gets whether or not this user is an admin in the {@link Room room}.
     *
     * @return True if an admin; false otherwise.
     */
    boolean isAdmin();

}
