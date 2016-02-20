package xyz.nickr.jitter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import xyz.nickr.jitter.api.Message;
import xyz.nickr.jitter.api.MessageHistory;
import xyz.nickr.jitter.api.Room;
import xyz.nickr.jitter.impl.event.MessageReceivedEventImpl;

public class JitterPoller {

    private final Jitter jitter;
    private final ScheduledExecutorService executor;

    JitterPoller(Jitter jitter) {
        this.jitter = jitter;
        this.executor = Executors.newScheduledThreadPool(10);
    }

    public Jitter getJitter() {
        return jitter;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public void subscribe(String roomId) {
        Room room = jitter.getRoom(roomId);
        if (room != null)
            executor.schedule(new RoomPoller(room), 10, TimeUnit.SECONDS);
    }

    public void stop() {
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    class RoomPoller implements Runnable {

        private final Room room;

        private MessageHistory history;

        public RoomPoller(Room room) {
            this.room = room;
            this.history = room.getMessageHistory();
            processMessages();
        }

        @Override
        public void run() {
            Message latest = history.getLatest();
            MessageHistory after = room.getMessagesAfter(latest);
            after.fullyLoad();
            this.history = after;
            processMessages();
        }

        public void processMessages() {
            for (Message message : history.getMessages()) {
                if (!message.isRead())
                    jitter.events().on(new MessageReceivedEventImpl(message));
            }
            this.history.markRead();
        }

    }

}
