package com.loyaltyplant.server.services.externalordering.application.helper;


import com.loyaltyplant.common.integration.protocol.digitalordering.response.CreatePosOrderResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.HealthcheckResponse;

import java.util.Map;

public class TestData {

    public static HealthcheckResponse healthcheckResponse(int salesOutletId) {
        HealthcheckResponse resp = new HealthcheckResponse();
        resp.setServices(Map.of("app", "UP", "db", "UP", "pos-" + salesOutletId, "UP"));
        return resp;
    }

    public static CreatePosOrderResponse createOrderResponse(String posId) {
        return new CreatePosOrderResponse(posId);
    }
}
