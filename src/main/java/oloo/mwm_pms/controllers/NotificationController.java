package oloo.mwm_pms.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api")
public class NotificationController {

    // A queue to store the notifications
    private final ConcurrentLinkedQueue<String> notificationsQueue = new ConcurrentLinkedQueue<>();

    // A list of connected clients
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // Endpoint to handle server-sent events
    @GetMapping(value = "/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> getNotifications() {
        SseEmitter emitter = new SseEmitter();

        // Add the emitter to the list of clients
        emitters.add(emitter);

        // Send notifications to the client
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        // Send existing notifications
        while (!notificationsQueue.isEmpty()) {
            try {
                emitter.send(notificationsQueue.poll());
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }

        return ResponseEntity.ok(emitter);
    }

    // Method to add a new notification
    public void addNotification(String notification) {
        notificationsQueue.add(notification);
        // Broadcast the notification to all connected clients
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(notification);
            } catch (Exception e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        }
    }
}
