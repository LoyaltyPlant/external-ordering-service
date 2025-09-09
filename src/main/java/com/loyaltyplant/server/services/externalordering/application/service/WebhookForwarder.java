package com.loyaltyplant.server.services.externalordering.application.service;

import com.loyaltyplant.server.services.externalordering.application.dto.OrdersWebhookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WebhookForwarder {

    @Value("${lp.webhook.url:https://release.loyaltyplant.com/digitalordering/pos/v2/integrated}")
    private String lpWebhookUrl;

    private final RestTemplate restTemplate;

    public void forward(Integer salesOutletId, String authTokenPlain, OrdersWebhookRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("SalesOutletId", String.valueOf(salesOutletId));
        headers.add("AuthorizationToken", authTokenPlain);

        HttpEntity<OrdersWebhookRequest> entity = new HttpEntity<>(request, headers);
        restTemplate.exchange(lpWebhookUrl, HttpMethod.POST, entity, Void.class);
    }
}
