package com.loyaltyplant.server.services.externalordering.application.controller.v1;


import com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.GetPosOrdersRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.CreatePosOrderResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.GetPosOrdersResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.HealthcheckResponse;
import com.loyaltyplant.server.services.externalordering.application.dto.OrdersWebhookRequest;
import com.loyaltyplant.server.services.externalordering.application.dto.PosVendorWebhookRequest;
import com.loyaltyplant.server.services.externalordering.application.utils.VendorToDoMapper;
import com.loyaltyplant.server.services.externalordering.application.dto.MenuEnvelope;
import com.loyaltyplant.server.services.externalordering.application.mock.PosMockService;
import com.loyaltyplant.server.services.externalordering.application.service.WebhookForwarder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.loyaltyplant.server.services.externalordering.application.mock.PosMockService.AUTH_TOKEN_HEADER;

@RestController
@RequestMapping("/api/v1/external-ordering")
@RequiredArgsConstructor
public class PosIntegrationControllerV1 {

    private final PosMockService posMockService;
    private final VendorToDoMapper mapper;
    private final WebhookForwarder forwarder;

    @GetMapping("/healthcheck")
    public ResponseEntity<HealthcheckResponse> healthcheck(
            @RequestHeader("SalesOutletId") Integer salesOutletId,
            @RequestHeader("AuthorizationToken") String authToken) {

        HealthcheckResponse status = posMockService.healthcheck(salesOutletId, authToken);

        return ResponseEntity.ok(status);
    }

    @PostMapping(
            path = "/orders",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetPosOrdersResponse> getOrders(
            @RequestHeader("SalesOutletId") Integer salesOutletId,
            @RequestHeader(value = "AuthorizationToken", required = false) String authToken,
            @RequestBody GetPosOrdersRequest request
    ) {
        return ResponseEntity.ok(posMockService.getOrders(salesOutletId, authToken, request));
    }

    @PostMapping(path = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatePosOrderResponse> createOrder(
            @RequestHeader("SalesOutletId") Integer salesOutletId,
            @RequestHeader(value = AUTH_TOKEN_HEADER, required = false) String authToken,
            @RequestBody CreatePosOrderRequest request
    ) {
        CreatePosOrderResponse response = posMockService.createOrder(salesOutletId, authToken, request);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_TOKEN_HEADER, posMockService.mockResponseToken(authToken));

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/menu")
    public ResponseEntity<MenuEnvelope> getMenu(
            @RequestHeader("SalesOutletId") Integer salesOutletId,
            @RequestHeader("AuthorizationToken") String authToken
    ) {
        return ResponseEntity.ok(posMockService.getMenu(salesOutletId, authToken));
    }

    @PostMapping("/webhook/orders")
    public ResponseEntity<Void> orderStatusWebhookFromPos(
            @RequestHeader("SalesOutletId") Integer salesOutletId,
            @RequestHeader("AuthorizationToken") String authTokenPlain,
            @RequestBody PosVendorWebhookRequest vendorRequest
    ) {
        OrdersWebhookRequest doRequest = mapper.toDoWebhook(vendorRequest);
        forwarder.forward(salesOutletId, authTokenPlain, doRequest);
        return ResponseEntity.ok().build();
    }
}
