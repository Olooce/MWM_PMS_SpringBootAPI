package oloo.mwm_pms.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final ConcurrentLinkedQueue<String> notificationsQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private static final String CONNECTION_SUCCESS_MESSAGE = "Login Successful!";

    @GetMapping(value = "/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> getNotifications(@RequestParam String clientId) {
        SseEmitter emitter = new SseEmitter();

        // Add the emitter to the map with the client ID
        emitters.put(clientId, emitter);

        // Send the connection success message
        try {
            emitter.send(CONNECTION_SUCCESS_MESSAGE);
        } catch (Exception e) {
            emitter.completeWithError(e);
            emitters.remove(clientId);
        }

        // Send existing notifications to the specific client
        while (!notificationsQueue.isEmpty()) {
            try {
                emitter.send(notificationsQueue.poll());
            } catch (Exception e) {
                emitter.completeWithError(e);
                emitters.remove(clientId);
            }
        }

        emitter.onCompletion(() -> emitters.remove(clientId));
        emitter.onTimeout(() -> emitters.remove(clientId));

        return ResponseEntity.ok(emitter);
    }

    public void addNotification(String notification) {
        notificationsQueue.add(notification);
        for (SseEmitter emitter : emitters.values()) {
            try {
                emitter.send(notification);
            } catch (Exception e) {
                emitter.completeWithError(e);
                // Optionally remove the emitter if needed
            }
        }
    }

    public void addNotificationToClient(String clientId, String notification) {
        SseEmitter emitter = emitters.get(clientId);
        if (emitter != null) {
            try {
                emitter.send(notification);
            } catch (Exception e) {
                emitter.completeWithError(e);
                emitters.remove(clientId);
            }
        }
    }
}
