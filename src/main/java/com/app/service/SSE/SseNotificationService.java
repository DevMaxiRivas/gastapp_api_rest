package com.app.service.SSE;

import com.app.event.transaction.TransactionCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseNotificationService {

    // This variable manages the emitters for all users
    private final Map<String, Map<String, SseEmitter>> userEmitters = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 20000)
    public void sendHeartbeat() {
        if (userEmitters.isEmpty()) return;
        userEmitters.forEach((userId, connections) -> {
            connections.entrySet().removeIf(entry -> {
                SseEmitter emitter = entry.getValue();
                try {
                    emitter.send(
                            SseEmitter.event().comment("heartbeat")
                    );
                    return false;
                } catch (Exception e) {
                    log.warn("Fail heartbeat for user {}, disconnecting: {}", userId, entry.getKey());
                    emitter.complete();
                    return true;
                }
            });

            if (connections.isEmpty()) {
                userEmitters.remove(userId);
            }
        });
    }

    public void addEmitter(String userId, SseEmitter emitter) {
        String connectionId = userId + "_" + UUID.randomUUID().toString();
        System.out.println("Emitter added with connectionId: " + connectionId);

        Map<String, SseEmitter> connections = userEmitters.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        connections.put(connectionId, emitter);

        Runnable cleanup = () -> {
            connections.remove(connectionId);
            if (connections.isEmpty()) {
                userEmitters.remove(userId);
            }
        };

        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError((e) -> {
            cleanup.run();
        });
    }

    @Async
    @EventListener
    public void handleTransactionCreated(TransactionCreatedEvent event) {
        Map<String, SseEmitter> userConnections = userEmitters.get(event.userId());
        if (userConnections != null) {
//            System.out.println(userConnections.size() + " connections created");
            userConnections.forEach((id, emitter) -> {
                try {
                    emitter.send(
                        SseEmitter.event()
                        .name("transaction-created")
                        .data("New transaction saved")
                    );
                } catch (Exception e) {
                    emitter.complete();
                    userConnections.remove(id);
                }
            });
        }
    }
}