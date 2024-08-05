package com.tima.web;

import ch.rasc.sse.eventbus.SseEvent;
import ch.rasc.sse.eventbus.SseEventBus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(value = "/jobs")
public class StreamController {
    SseEventBus eventBus;

    public StreamController(SseEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @GetMapping(path = "/progress/{id}")
    @CrossOrigin
    public SseEmitter progress(@PathVariable("id") String clientId) {
        return this.eventBus.createSseEmitter(clientId, SseEvent.DEFAULT_EVENT);
    }
}
