package com.loyaltyplant.server.services.externalordering.service;

import com.loyaltyplant.common.integration.protocol.digitalordering.model.v2.response.MenuAggregatorV2;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.GetPosOrdersRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.CreatePosOrderResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.GetPosOrdersResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.HealthcheckResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.webhook.OrdersWebhookRequest;
import com.loyaltyplant.server.services.externalordering.model.PosVendorWebhookRequest;
import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * This interface provides methods for converting data from external POS systems to LP Digital Ordering format.
 */
public interface IExternalPosConvertingService {
    /**
     * This method check health of external POS system amd service itself.
     * @param salesOutletId - sales outlet ID
     * @return Optional of HealthcheckResponse with healthcheck data or empty if conversion failed
     */
    @Nonnull Optional<HealthcheckResponse> healthcheck(@Nonnull Integer salesOutletId);

    /**
     * This method converts external POS system orders data to LP Digital Ordering format.
     * @param salesOutletId - sales outlet ID
     * @param request - GetPosOrdersRequest with orders data
     * @return Optional of GetPosOrdersResponse with orders data or empty if conversion failed
     */
    @Nonnull Optional<GetPosOrdersResponse> getOrders(@Nonnull Integer salesOutletId, @Nonnull GetPosOrdersRequest request);

    /**
     * This method creates order in external POS system menu and returns response in LP Digital Ordering format.
     * @param salesOutletId - sales outlet ID
     * @param request - CreatePosOrderRequest with order data
     * @return Optional of CreatePosOrderResponse with order data or empty if conversion failed
     */
    @Nonnull Optional<CreatePosOrderResponse> createOrder(@Nonnull Integer salesOutletId, @Nonnull CreatePosOrderRequest request);

    /**
     * This method converts external POS system menu to LP Digital Ordering menu format.
     * @param salesOutletId - sales outlet ID
     * @return Optional of MenuAggregatorV2 with menu data or empty if conversion failed
     */
    @Nonnull Optional<MenuAggregatorV2> getMenu(@Nonnull Integer salesOutletId);

    /**
     * Converts a vendor-specific webhook payload into LP Digital Ordering webhook request.
     * @param vendorRequest vendor webhook payload
     * @return Optional with Digital Ordering webhook request or empty if conversion failed
     */
    @Nonnull Optional<OrdersWebhookRequest> webhook(@Nonnull PosVendorWebhookRequest vendorRequest);
}