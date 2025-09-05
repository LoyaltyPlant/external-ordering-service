package com.loyaltyplant.server.services.externalordering.application.helper;

import lombok.Data;

@Data
public class OrderOperationResult {

    private Result result;

    private String orderId;

    private String posVendor;

    public enum Result {
        ORDER_CREATED,
        ORDER_MODIFIED,
        ORDER_CANCELLED,
        ORDER_OPERATION_FAILED
    }
}