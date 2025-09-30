package com.loyaltyplant.server.services.externalordering.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PosVendorWebhookRequest {
    private List<PosOrder> orders;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PosOrder {
        private String externalId;
        private String queueNumber;
        private String status;
    }
}