package xyz.nickr.jitter.api;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A utility class for dealing with listeners.
 *
 * @author Nick Robson
 * @param <T> The listener type.
 */
public class ListenerSet<T> {

    private final List<T> listeners = new LinkedList<>();

    /**
     * Adds a listener to this set.
     *
     * @param listener The listener.
     */
    public void add(T listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }

    /**
     * Runs a {@link Consumer} with each listener in this set.
     *
     * @param consumer The consumer.
     */
    public void forEach(Consumer<T> consumer) {
        Objects.requireNonNull(consumer, "consumer");
        listeners.forEach(l -> {
            try {
                consumer.accept(l);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
