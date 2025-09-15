package com.loyaltyplant.server.services.externalordering.utils;

public final class Consts {
    public static final String AUTH_TOKEN_HEADER = "AuthorizationToken";
    public static final String SALES_OUTLET_HEADER = "SalesOutletId";

    public static final int WEBHOOK_TO_LP_CONNECT_TIMEOUT_SECONDS = 3;
    public static final int WEBHOOK_TO_LP_REQUEST_READ_TIMEOUT_SECONDS = 5;

    private Consts() {
        throw new UnsupportedOperationException("Utility class");
    }
}
