package com.loyaltyplant.server.services.externalordering.application.controller;

import com.loyaltyplant.common.integration.protocol.digitalordering.MenuAggregator;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.Order;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.GetPosOrdersRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.CreatePosOrderResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.GetPosOrdersResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.HealthcheckResponse;
import com.loyaltyplant.server.services.externalordering.application.helper.OrderOperationResult;
import com.loyaltyplant.server.services.externalordering.application.mock.PosMockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/external-ordering")
@RequiredArgsConstructor
public class PosIntegrationController {

    private final PosMockService posMockService;

    @GetMapping("/health/{salesOutletId}")
    public ResponseEntity<HealthcheckResponse> healthcheck(@PathVariable Integer salesOutletId) {
        return ResponseEntity.ok(posMockService.healthcheck(salesOutletId));
    }

    @PostMapping("/orders/{salesOutletId}")
    public ResponseEntity<GetPosOrdersResponse> getOrders(
            @PathVariable Integer salesOutletId,
            @RequestBody GetPosOrdersRequest request) {
        return ResponseEntity.ok(posMockService.getOrders(salesOutletId, request));
    }

    @PostMapping("/order/{salesOutletId}")
    public ResponseEntity<CreatePosOrderResponse> createOrder(
            @PathVariable Integer salesOutletId,
            @RequestBody CreatePosOrderRequest request) {
        return ResponseEntity.ok(posMockService.createOrder(salesOutletId, request));
    }

    @PutMapping("/order/{salesOutletId}")
    public ResponseEntity<OrderOperationResult> updateOrder(
            @PathVariable Integer salesOutletId,
            @RequestBody Order order) {
        return ResponseEntity.ok(posMockService.updateOrder(salesOutletId, order));
    }

    @PostMapping("/order/approve/{salesOutletId}")
    public ResponseEntity<OrderOperationResult> approveOrder(
            @PathVariable Integer salesOutletId,
            @RequestBody Order order) {
        return ResponseEntity.ok(posMockService.approveOrder(salesOutletId, order));
    }

    @PostMapping("/order/cancel/{salesOutletId}")
    public ResponseEntity<OrderOperationResult> cancelOrder(
            @PathVariable Integer salesOutletId,
            @RequestBody Order order) {
        return ResponseEntity.ok(posMockService.cancelOrder(salesOutletId, order));
    }

    @GetMapping("/menu/{salesOutletId}")
    public ResponseEntity<MenuAggregator> getMenu(
            @PathVariable Integer salesOutletId,
            @RequestParam String posEstablishmentId) {
        return ResponseEntity.ok(posMockService.getMenu(salesOutletId, posEstablishmentId));
    }

    @PostMapping("/reload")
    public ResponseEntity<Void> reload() {
        posMockService.reload();
        return ResponseEntity.ok().build();
    }
}
