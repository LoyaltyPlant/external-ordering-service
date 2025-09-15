package com.loyaltyplant.server.services.externalordering.service.impl;

import com.loyaltyplant.common.integration.protocol.digitalordering.webhook.OrdersWebhookRequest;
import com.loyaltyplant.server.services.externalordering.service.IExternalWebhookForwarder;
import com.loyaltyplant.server.services.externalordering.service.ITokenValidationService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.loyaltyplant.server.services.externalordering.utils.Consts.AUTH_TOKEN_HEADER;
import static com.loyaltyplant.server.services.externalordering.utils.Consts.SALES_OUTLET_HEADER;

@Service
@Log4j2
public class WebhookForwarder implements IExternalWebhookForwarder {
    private final RestTemplate restTemplate;
    private final ITokenValidationService tokenValidationService;
    private final String lpWebhookUrl;

    @Autowired
    public WebhookForwarder(final @Qualifier("webhookRestTemplate") RestTemplate restTemplate,
                            final ITokenValidationService tokenValidationService,
                            final @Value("${lp.webhook.url}") String lpWebhookUrl) {
        this.restTemplate = restTemplate;
        this.tokenValidationService = tokenValidationService;
        this.lpWebhookUrl = Objects.requireNonNull(lpWebhookUrl, "lp.webhook.url is required");
    }

    @Override
    public boolean forward(final @NotNull Integer salesOutletId, final @NotNull OrdersWebhookRequest request) {
        Objects.requireNonNull(salesOutletId, "salesOutletId is required");
        Objects.requireNonNull(request, "request is required");

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(SALES_OUTLET_HEADER, String.valueOf(salesOutletId));

        final String responseToken = Objects.requireNonNull(
                tokenValidationService.getResponseToken(salesOutletId),
                "response token must not be null"
        );
        headers.set(AUTH_TOKEN_HEADER, responseToken);


        final HttpEntity<OrdersWebhookRequest> entity = new HttpEntity<>(request, headers);

        try {
            final ResponseEntity<Void> response = restTemplate.exchange(lpWebhookUrl, HttpMethod.POST, entity, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.error("Failed to forward webhook to LP");
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to forward webhook to LP", e);
            return false;
        }
    }
}
