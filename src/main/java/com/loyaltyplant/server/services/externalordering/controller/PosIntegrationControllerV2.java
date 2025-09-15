package com.loyaltyplant.server.services.externalordering.controller;

import com.loyaltyplant.common.integration.protocol.digitalordering.model.v2.response.MenuAggregatorV2;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.GetPosOrdersRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.CreatePosOrderResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.GetPosOrdersResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.HealthcheckResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.webhook.OrdersWebhookRequest;
import com.loyaltyplant.server.services.externalordering.model.PosVendorWebhookRequest;
import com.loyaltyplant.server.services.externalordering.service.IExternalPosConvertingService;
import com.loyaltyplant.server.services.externalordering.service.IExternalWebhookForwarder;
import com.loyaltyplant.server.services.externalordering.service.ITokenValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.loyaltyplant.server.services.externalordering.utils.Consts.AUTH_TOKEN_HEADER;
import static com.loyaltyplant.server.services.externalordering.utils.Consts.SALES_OUTLET_HEADER;

@RestController
@RequestMapping("/api/v2")
@Tag(name = "External POS API")
public class PosIntegrationControllerV2 {
    private final IExternalPosConvertingService convertingService;
    private final ITokenValidationService tokenValidationService;
    private final IExternalWebhookForwarder forwarder;

    @Autowired
    public PosIntegrationControllerV2(final IExternalPosConvertingService convertingService,
                                      final ITokenValidationService tokenValidationService,
                                      final IExternalWebhookForwarder forwarder) {
        this.convertingService = convertingService;
        this.tokenValidationService = tokenValidationService;
        this.forwarder = forwarder;
    }

    @GetMapping("/healthcheck")
    @Operation(summary = "Check service and POS health",
            description = "Verifies if the POS and service is operational and ready to accept requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Service is operational",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"POS\": \"UP\", \"Database\": \"DOWN\"}"
                            ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication"),
            @ApiResponse(responseCode = "503", description = "Service is unavailable")
    })
    public ResponseEntity<HealthcheckResponse> healthcheck(@RequestHeader(SALES_OUTLET_HEADER) Integer salesOutletId) {
        final Optional<HealthcheckResponse> status = convertingService.healthcheck(salesOutletId);
        return status.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }

    @PostMapping(path = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve orders",
            description = "Fetches a list of orders based on the provided criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved orders"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication"),
            @ApiResponse(responseCode = "503", description = "Service temporarily unavailable")
    })
    public ResponseEntity<GetPosOrdersResponse> getOrders(final @RequestHeader(SALES_OUTLET_HEADER) Integer salesOutletId,
                                                          final @RequestBody GetPosOrdersRequest request) {
        final Optional<GetPosOrdersResponse> orders = convertingService.getOrders(salesOutletId, request);
        return orders.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }

    @PostMapping(path = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new order",
            description = "Creates a new order in the POS system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid order data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication"),
            @ApiResponse(responseCode = "503", description = "Service temporarily unavailable")
    })
    public ResponseEntity<CreatePosOrderResponse> createOrder(final @RequestHeader(SALES_OUTLET_HEADER) Integer salesOutletId,
                                                              final @RequestBody CreatePosOrderRequest request) {
        final Optional<CreatePosOrderResponse> order = convertingService.createOrder(salesOutletId, request);

        if (order.isEmpty()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_TOKEN_HEADER, tokenValidationService.getResponseToken(salesOutletId));

        return new ResponseEntity<>(order.get(), headers, HttpStatus.OK);
    }

    @GetMapping("/menu")
    @Operation(summary = "Get menu items",
            description = "Retrieves the current menu with all available items and categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved menu"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication"),
            @ApiResponse(responseCode = "503", description = "Service temporarily unavailable")
    })
    public ResponseEntity<MenuAggregatorV2> getMenu(@RequestHeader(SALES_OUTLET_HEADER) Integer salesOutletId) {
        final Optional<MenuAggregatorV2> menu = convertingService.getMenu(salesOutletId);
        return menu.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }

    @PostMapping("/webhook/orders")
    @Operation(summary = "Order status webhook",
            description = "Receives order status updates from the POS system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Webhook processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid webhook data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication"),
            @ApiResponse(responseCode = "503", description = "Service temporarily unavailable")
    })
    public ResponseEntity<Void> orderStatusWebhookFromPos(@RequestHeader(SALES_OUTLET_HEADER) Integer salesOutletId,
                                                          @RequestBody PosVendorWebhookRequest vendorRequest) {
        final Optional<OrdersWebhookRequest> webhook = convertingService.webhook(vendorRequest);

        if (webhook.isEmpty()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } else {
            if (forwarder.forward(salesOutletId, webhook.get())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
        }
    }
}
