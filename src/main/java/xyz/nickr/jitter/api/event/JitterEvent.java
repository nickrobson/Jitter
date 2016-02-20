package xyz.nickr.jitter.api.event;

import xyz.nickr.jitter.Jitter;

public interface JitterEvent {

    /**
     * Gets the providing {@link Jitter} object.
     *
     * @return The provider.
     */
    Jitter getJitter();

}
