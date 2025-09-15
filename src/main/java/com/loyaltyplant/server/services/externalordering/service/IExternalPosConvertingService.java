package com.loyaltyplant.server.services.externalordering.service;

import com.loyaltyplant.common.integration.protocol.digitalordering.model.v2.response.MenuAggregatorV2;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.GetPosOrdersRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.CreatePosOrderResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.GetPosOrdersResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.HealthcheckResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.webhook.OrdersWebhookRequest;
import com.loyaltyplant.server.services.externalordering.model.PosVendorWebhookRequest;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

/**
 * This interface provides methods for converting digital ordering protocol data to data of external POS systems.
 */
public interface IExternalPosConvertingService {
    /**
     * This method converts digital ordering healthcheck data to response of external POS system.
     * @param salesOutletId - sales outlet ID
     * @return Optional of HealthcheckResponse with healthcheck data or empty if conversion failed
     */
    Optional<HealthcheckResponse> healthcheck(@NotNull Integer salesOutletId);

    /**
     * This method converts digital ordering orders data to response of external POS system.
     * @param salesOutletId - sales outlet ID
     * @param request - GetPosOrdersRequest with orders data
     * @return Optional of GetPosOrdersResponse with orders data or empty if conversion failed
     */
    Optional<GetPosOrdersResponse> getOrders(@NotNull Integer salesOutletId, @NotNull GetPosOrdersRequest request);

    /**
     * This method converts digital ordering create order data to response of external POS system.
     * @param salesOutletId - sales outlet ID
     * @param request - CreatePosOrderRequest with order data
     * @return Optional of CreatePosOrderResponse with order data or empty if conversion failed
     */
    Optional<CreatePosOrderResponse> createOrder(@NotNull Integer salesOutletId, @NotNull CreatePosOrderRequest request);

    /**
     * This method converts digital ordering menu data to response of external POS system.
     * @param salesOutletId - sales outlet ID
     * @return Optional of MenuAggregatorV2 with menu data or empty if conversion failed
     */
    Optional<MenuAggregatorV2> getMenu(@NotNull Integer salesOutletId);

    /**
     * This method converts digital ordering webhook data to response of external POS system.
     * @param vendorRequest - PosVendorWebhookRequest with webhook data
     * @return Optional of OrdersWebhookRequest with webhook data or empty if conversion failed
     */
    Optional<OrdersWebhookRequest> webhook(@NotNull PosVendorWebhookRequest vendorRequest);
}