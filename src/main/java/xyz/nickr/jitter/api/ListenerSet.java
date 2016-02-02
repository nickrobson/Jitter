package xyz.nickr.jitter.api;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ListenerSet<T> {

    private final List<T> listeners = new LinkedList<>();

    public void add(T listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }

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
