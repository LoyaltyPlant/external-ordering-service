package com.loyaltyplant.server.services.externalordering.application.dto;

import com.loyaltyplant.common.integration.protocol.digitalordering.order.Order;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersWebhookRequest {
    private List<Order> orders;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderPatch {
        private String id;
        private String queueNumber;
        private State state;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class State {
            private String status;
        }
    }
}