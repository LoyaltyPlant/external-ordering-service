package com.loyaltyplant.server.services.externalordering.application.utils;

import com.loyaltyplant.common.integration.protocol.digitalordering.order.Order;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.OrderState;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.OrderStatus;
import com.loyaltyplant.server.services.externalordering.application.dto.OrdersWebhookRequest;
import com.loyaltyplant.server.services.externalordering.application.dto.PosVendorWebhookRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VendorToDoMapper {

    public OrdersWebhookRequest toDoWebhook(PosVendorWebhookRequest src) {
        OrdersWebhookRequest dst = new OrdersWebhookRequest();
        List<Order> orders = src.getOrders().stream().map(patch -> {
            Order o = new Order();
            o.setId(patch.getExternalId());
            o.setQueueNumber(patch.getQueueNumber());
            OrderStatus status;
            try {
                status = OrderStatus.valueOf(patch.getStatus());
            } catch (IllegalArgumentException e) {
                status = OrderStatus.FAILED;
            }
            o.setState(OrderState.builder().status(status).build());
            return o;
        }).toList();
        dst.setOrders(orders);
        return dst;
    }
}