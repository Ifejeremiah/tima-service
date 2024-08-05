package com.tima.service;

import ch.rasc.sse.eventbus.SseEvent;
import ch.rasc.sse.eventbus.SseEventBus;
import com.tima.model.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StreamService {
    SseEventBus eventBus;
    @Value("${MAX.SIZE}")
    Integer maxSize;
    Integer index;

    public StreamService(SseEventBus eventBus) {
        this.eventBus = eventBus;
        this.index = 1;
    }

    @Scheduled(fixedDelayString = "${SCHEDULER.DELAY}")
    public void sendStream() {
        Stream stream = new Stream();
        stream.setId(1);
        stream.setRecordCount(maxSize);
        stream.setPreparedCount(index);
        stream.setStatus("SCHEDULED");

        this.eventBus.handleEvent(SseEvent.ofData(stream));
        index++;
    }
}
