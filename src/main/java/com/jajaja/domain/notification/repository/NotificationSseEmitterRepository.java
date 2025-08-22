package com.jajaja.domain.notification.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class NotificationSseEmitterRepository {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final Map<Long, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter add(Long memberId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitter.onCompletion(() -> remove(memberId, emitter));
        emitter.onTimeout(() -> remove(memberId, emitter));
        emitter.onError((ex) -> remove(memberId, emitter));

        emitters.computeIfAbsent(memberId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        try {
            emitter.send(SseEmitter.event().comment("open"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void send(Long memberId, Object data) {
        List<SseEmitter> memberEmitters = emitters.get(memberId);
        if (memberEmitters == null || memberEmitters.isEmpty()) {
            return;
        }

        List<SseEmitter> dead = new ArrayList<>();
        for (SseEmitter emitter : memberEmitters) {
            try {
                emitter.send(SseEmitter.event().name("alarm").data(data));
            } catch (IOException e) {
                dead.add(emitter);
            }
        }

        if (!dead.isEmpty()) {
            memberEmitters.removeAll(dead);
        }
    }

    private void remove(Long memberId, SseEmitter emitter) {
        List<SseEmitter> memberEmitters = emitters.get(memberId);
        if (memberEmitters == null) {
            return;
        }
        memberEmitters.remove(emitter);
        if (memberEmitters.isEmpty()) {
            emitters.remove(memberId);
        }
    }
}
