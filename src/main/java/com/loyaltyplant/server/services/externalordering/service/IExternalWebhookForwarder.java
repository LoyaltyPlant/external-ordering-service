package com.loyaltyplant.server.services.externalordering.service;

import com.loyaltyplant.common.integration.protocol.digitalordering.webhook.OrdersWebhookRequest;
import jakarta.annotation.Nonnull;

/**
 * Interface for forwarding webhook notifications from external systems to LP.
 * Implementations of this interface are responsible for transmitting order status updates
 * to the appropriate endpoints with proper authentication and error handling.
 */
public interface IExternalWebhookForwarder {
    /**
     * Forwards an order status update to the LP backend.
     *
     * @param salesOutletId The unique identifier of the sales outlet associated with the order.
     *                      Must not be null.
     * @param request The order status update request containing the order details and new status.
     *                Must not be null.
     * @return true if the webhook was successfully forwarded and acknowledged by the external system,
     *         false if the forwarding failed or the external system returned an error response.
     * @throws NullPointerException if either salesOutletId or request is null.
     */
    boolean forward(@Nonnull Integer salesOutletId, @Nonnull OrdersWebhookRequest request);
}
