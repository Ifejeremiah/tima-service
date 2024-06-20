package com.tima.web.advice;

import com.tima.model.Response;
import com.tima.model.Webhook;
import com.tima.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/webhooks/trigger")
public class WebhookController {
    WebhookService webhookService;

    @Autowired
    WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Response<String> trigger(@Validated @RequestBody Webhook webhook) {
        webhookService.trigger(webhook);
        return new Response<>("Webhook triggered successfully");
    }
}
