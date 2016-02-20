package xyz.nickr.jitter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import xyz.nickr.jitter.api.event.JitterEvent;
import xyz.nickr.jitter.api.event.JitterListener;

public class JitterEvents {

    final Jitter jitter;
    final Map<Class<?>, Set<ListenerInfo>> methods;

    JitterEvents(Jitter jitter) {
        this.jitter = jitter;
        this.methods = new HashMap<>();
    }

    /**
     * Registers a {@link JitterListener listener}.
     *
     * @param listener The listener.
     */
    public void register(JitterListener listener) {
        if (listener == null)
            return;
        Class<?> cls = listener.getClass();
        for (Method method : cls.getMethods()) {
            Class<?>[] params = method.getParameterTypes();
            if (params.length == 1 && JitterEvent.class.isAssignableFrom(params[0])) {
                method.setAccessible(true);
                methods.merge(params[0], new HashSet<>(Arrays.asList(new ListenerInfo(listener, method))), this::merge);
            }
        }
    }

    /**
     * Runs an event through all appropriate listeners.
     *
     * @param event The event.
     */
    public void on(JitterEvent event) {
        if (event == null)
            return;
        for (Entry<Class<?>, Set<ListenerInfo>> entry : methods.entrySet())
            if (entry.getValue() != null && entry.getKey().isAssignableFrom(event.getClass()))
                entry.getValue().forEach(i -> i.run(event));
    }

    <T> Set<T> merge(Set<T> a, Set<T> b) {
        Set<T> set = new HashSet<>(a);
        set.addAll(b);
        return set;
    }

    static class ListenerInfo {

        final Object object;
        final Method method;

        public ListenerInfo(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        public void run(JitterEvent event) {
            try {
                method.invoke(object, event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
