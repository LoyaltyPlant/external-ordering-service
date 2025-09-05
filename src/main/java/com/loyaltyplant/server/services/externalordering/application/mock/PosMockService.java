package com.loyaltyplant.server.services.externalordering.application.mock;

import com.loyaltyplant.common.integration.protocol.digitalordering.MenuAggregator;
import com.loyaltyplant.common.integration.protocol.digitalordering.model.*;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.Order;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.OrderState;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.OrderStatus;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.GetPosOrdersRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.CreatePosOrderResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.GetPosOrdersResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.HealthcheckResponse;
import com.loyaltyplant.server.services.externalordering.application.helper.OrderOperationResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PosMockService {

    public HealthcheckResponse healthcheck(Integer salesOutletId) {
        Map<String, String> services = new LinkedHashMap<>();
        services.put("app", "UP");
        services.put("db", "UP");
        services.put("pos-" + salesOutletId, "UP");

        HealthcheckResponse resp = new HealthcheckResponse();
        resp.setServices(services);
        return resp;
    }

    public GetPosOrdersResponse getOrders(Integer salesOutletId, GetPosOrdersRequest request) {
        Order o1 = Order.builder()
                .id("123")
                .state(OrderState.builder()
                        .status(OrderStatus.AWAITING_DELIVERY)
                        .reason("test_reason")
                        .build())
                .comment("Mock order for testing")
                .queueNumber("Q1")
                .build();

        Order o2 = Order.builder()
                .id("456")
                .state(OrderState.builder()
                        .status(OrderStatus.PROCESSED)
                        .reason("test_reason")
                        .build())
                .comment("Second mock order")
                .queueNumber("Q2")
                .build();

        return new GetPosOrdersResponse(List.of(o1, o2));
    }

    public CreatePosOrderResponse createOrder(Integer salesOutletId, CreatePosOrderRequest request) {
        return new CreatePosOrderResponse("789");
    }

    public OrderOperationResult updateOrder(Integer salesOutletId, Order order) {
        return mockResult(order, OrderOperationResult.Result.ORDER_MODIFIED);
    }

    public OrderOperationResult approveOrder(Integer salesOutletId, Order order) {
        return mockResult(order, OrderOperationResult.Result.ORDER_MODIFIED);
    }

    public OrderOperationResult cancelOrder(Integer salesOutletId, Order order) {
        return mockResult(order, OrderOperationResult.Result.ORDER_CANCELLED);
    }

    public MenuAggregator getMenu(Integer salesOutletId, String posEstablishmentId) {
        IntegrationCategory category = new IntegrationCategory();
        category.setId("cat-1");
        category.setTitle("Pizzas");

        IntegrationMenuItem item = new IntegrationMenuItem();
        item.setId("item-1");
        item.setTitle("Margherita");

        IntegrationMenuItemInCategory itemInCategory = IntegrationMenuItemInCategory.builder()
                .category(category)
                .sortOrder(1)
                .build();

        item.setCategories(List.of(itemInCategory));

        IntegrationModifier modifier = new IntegrationModifier();
        modifier.setId("mod-1");
        modifier.setTitle("Extra cheese");
        modifier.setCostPrice(BigDecimal.valueOf(1.50));

        IntegrationModifierGroupPrototype modifierGroup = new IntegrationModifierGroupPrototype();
        modifierGroup.setId("mg-1");
        modifierGroup.setTitle("Cheese options");
        IntegrationModifierInModifierGroup mig = new IntegrationModifierInModifierGroup();
        mig.setModifier(modifier);

        modifierGroup.setModifiers(List.of(mig));

        return MenuAggregator.builder()
                .categories(List.of(category))
                .items(List.of(item))
                .modifiers(List.of(modifier))
                .modifierGroups(List.of(modifierGroup))
                .build();
    }

    public void reload() {
    }

    private OrderOperationResult mockResult(Order order, OrderOperationResult.Result result) {
        OrderOperationResult res = new OrderOperationResult();
        res.setOrderId(order.getId() != null ? order.getId() : "MOCK_ORDER_ID");
        res.setResult(result);
        res.setPosVendor("MOCK_POS_VENDOR");
        return res;
    }
}

