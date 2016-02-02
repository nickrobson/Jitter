package xyz.nickr.jitter.api;

/**
 * Represents the different types of {@link Room rooms}.
 *
 * @author Nick Robson
 */
public enum RoomType {

    /**
     * A GitHub user.
     */
    USER,

    /**
     * A GitHub repository.
     */
    REPOSITORY,

    /**
     * A GitHub organisation.
     */
    ORGANISATION,

    /**
     * A Gitter channel for a user.
     */
    USER_CHANNEL,

    /**
     * A Gitter channel for a repository.
     */
    REPOSITORY_CHANNEL,

    /**
     * A Gitter channel for an organisation.
     */
    ORGANISATION_CHANNEL,

    /**
     * An unknown room type: used as a fallback
     */
    UNKNOWN;

}
