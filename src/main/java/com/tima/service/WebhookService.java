package com.tima.service;

import com.tima.model.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebhookService {
    @Value("${DEFAULT.BRANCH}")
    private String branch;

    public void trigger(Webhook webhook) {
        log.info("Webhook triggered successfully");

        if (webhook.getRef().equalsIgnoreCase(branch)) {
            log.info("Target branch is: {}", webhook.getRef());
            log.info("Proceeding to CI...");
        }
    }
}
