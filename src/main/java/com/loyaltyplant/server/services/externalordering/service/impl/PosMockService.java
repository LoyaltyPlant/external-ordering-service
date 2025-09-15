package com.loyaltyplant.server.services.externalordering.service.impl;

import com.loyaltyplant.common.integration.protocol.digitalordering.model.Price;
import com.loyaltyplant.common.integration.protocol.digitalordering.model.v2.*;
import com.loyaltyplant.common.integration.protocol.digitalordering.model.v2.response.MenuAggregatorV2;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.Order;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.OrderState;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.OrderStatus;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.OrderingType;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.GetPosOrdersRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.CreatePosOrderResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.GetPosOrdersResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.HealthcheckResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.webhook.OrdersWebhookRequest;
import com.loyaltyplant.server.services.externalordering.model.PosVendorWebhookRequest;
import com.loyaltyplant.server.services.externalordering.service.IExternalPosConvertingService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

import static com.loyaltyplant.server.services.externalordering.utils.Consts.HEALTH_UP;

@Service
public class PosMockService implements IExternalPosConvertingService {
    private final Meter.MeterProvider<Counter> createdOrdersCounter;

    @Autowired
    public PosMockService(final MeterRegistry registry) {
        // example of metric exported to prometheus
        this.createdOrdersCounter = Counter.builder("external_order_service_created_orders")
                .description("Created orders counter.")
                .withRegistry(registry);
    }


    @Override
    public @Nonnull Optional<HealthcheckResponse> healthcheck(final @Nonnull Integer salesOutletId) {
        Objects.requireNonNull(salesOutletId, "salesOutletId is required");

        final HealthcheckResponse resp = new HealthcheckResponse();
        resp.setServices(Map.of(
                "POS", HEALTH_UP,
                "DB", HEALTH_UP,
                "pos-" + salesOutletId, HEALTH_UP
        ));
        return Optional.of(resp);
    }

    @Override
    public @Nonnull Optional<GetPosOrdersResponse> getOrders(final @Nonnull Integer salesOutletId, final @Nonnull GetPosOrdersRequest request) {
        Objects.requireNonNull(salesOutletId, "salesOutletId is required");
        Objects.requireNonNull(request, "request is required");

        final List<Order> orders = new ArrayList<>();

        if (!CollectionUtils.isEmpty(request.getOrderPosIds())) {
            for (String orderPosId : request.getOrderPosIds()) {
                final Order order = Order.builder()
                        .id(orderPosId)
                        .state(OrderState.builder()
                                .status(OrderStatus.PROCESSING)
                                .reason("Mocked")
                                .build())
                        .comment("Mock order for SalesOutlet " + salesOutletId)
                        .queueNumber("Q" + orderPosId)
                        .build();

                orders.add(order);
            }
        }

        return Optional.of(new GetPosOrdersResponse(orders));
    }

    @Override
    public @Nonnull Optional<CreatePosOrderResponse> createOrder(final @Nonnull Integer salesOutletId, final @Nonnull CreatePosOrderRequest request) {
        Objects.requireNonNull(salesOutletId, "salesOutletId is required");
        Objects.requireNonNull(request, "request is required");

        final String generatedPosId = "POS-" + UUID.randomUUID();
        this.createdOrdersCounter.withTag("kind", "success").increment();

        return Optional.of(new CreatePosOrderResponse(generatedPosId));
    }

    @Override
    public @Nonnull Optional<MenuAggregatorV2> getMenu(final @Nonnull Integer salesOutletId) {
        Objects.requireNonNull(salesOutletId, "salesOutletId is required");

        final IntegrationCategoryV2 iceCream500 = IntegrationCategoryV2.builder()
                .id("2029171")
                .code("2029171")
                .title(Map.of("en", "Ice Cream 500 ml", "es", "Helado 500 ml", "fr", "Glace 500 ml"))
                .description(Map.of("en", "A 500 ml serving of ice cream.", "es", "Una porción de helado de 500 ml.", "fr", "Une portion de glace de 500 ml."))
                .root(false)
                .subCategories(List.of())
                .build();

        final IntegrationCategoryV2 sundaes = IntegrationCategoryV2.builder()
                .id("2029182")
                .code("2029182")
                .title(Map.of("en", "Ice Cream Sundaes", "es", "Helados Sundaes", "fr", "Coupes glacées"))
                .description(Map.of("en", "A delicious ice cream dessert with various toppings.", "es", "Un delicioso postre de helado...", "fr", "Un délicieux dessert glacé..."))
                .root(false)
                .subCategories(List.of())
                .build();

        final IntegrationCategoryV2 sweets = IntegrationCategoryV2.builder()
                .id("2029027")
                .code("2029027")
                .title(Map.of("en", "Sweets", "es", "Dulces", "fr", "Desserts"))
                .description(Map.of("en", "A variety of sweet treats.", "es", "Una variedad de dulces.", "fr", "Une variété de desserts sucrés."))
                .root(true)
                .rootSortOrder(1)
                .subCategories(List.of(
                        IntegrationSubCategoryInCategoryV2.builder()
                                .subCategory(iceCream500)
                                .sortOrder(1)
                                .build(),
                        IntegrationSubCategoryInCategoryV2.builder()
                                .subCategory(sundaes)
                                .sortOrder(2)
                                .build()
                ))
                .build();

        final IntegrationModifierV2 cheddar = IntegrationModifierV2.builder()
                .id("2029334")
                .code("2029334")
                .title(Map.of("en", "cheddar cheese", "es", "queso cheddar", "fr", "fromage cheddar"))
                .description(Map.of("en", "A slice of cheddar cheese.", "es", "Una rebanada...", "fr", "Une tranche..."))
                .pricesByType(priceAllTypes(BigDecimal.ZERO))
                .build();

        final IntegrationModifierV2 tomato = IntegrationModifierV2.builder()
                .id("2029337").code("2029337")
                .title(Map.of("en", "tomato sauce", "es", "salsa de tomate", "fr", "sauce tomate"))
                .description(Map.of("en", "A serving of tomato sauce.", "es", "Una porción...", "fr", "Une portion..."))
                .pricesByType(priceAllTypes(BigDecimal.ZERO))
                .build();

        final IntegrationModifierV2 bacon = IntegrationModifierV2.builder()
                .id("2029512")
                .code("2029512")
                .title(Map.of("en", "bacon", "es", "tocino", "fr", "bacon"))
                .description(Map.of("en", "A slice of crispy bacon.", "es", "Una rebanada...", "fr", "Une tranche..."))
                .pricesByType(priceAllTypes(new BigDecimal("0.85")))
                .build();

        final IntegrationModifierGroupPrototypeV2 group1 = IntegrationModifierGroupPrototypeV2.builder()
                .id("ing_2029111_0_0")
                .code("ing_2029111_0_0")
                .title(Map.of("en", "Ingredients for Morning Mouthful",
                        "es", "Ingredientes para el Bocado Matutino",
                        "fr", "Ingrédients pour la Bouchée Matinale"))
                .description(Map.of("en", "Choose your ingredients...", "es", "Elige tus ingredientes...", "fr", "Choisissez vos ingrédients..."))
                .modifiers(List.of(
                        IntegrationModifierInModifierGroupV2.builder()
                                .modifier(bacon)
                                .build(),
                        IntegrationModifierInModifierGroupV2.builder()
                                .modifier(tomato)
                                .build(),
                        IntegrationModifierInModifierGroupV2.builder()
                                .modifier(cheddar)
                                .build()
                ))
                .max(6).min(0).free(6)
                .build();

        final IntegrationModifierGroupPrototypeV2 group2 = IntegrationModifierGroupPrototypeV2.builder()
                .id("ing_2029111_0.85_0").code("ing_2029111_0.85_0")
                .title(Map.of("en", "Ingredients for Morning Mouthful 2",
                        "es", "Ingredientes para el Bocado Matutino 2",
                        "fr", "Ingrédients pour la Bouchée Matinale 2"))
                .description(Map.of("en", "Additional ingredients...", "es", "Ingredientes adicionales...", "fr", "Ingrédients supplémentaires..."))
                .modifiers(List.of(
                        IntegrationModifierInModifierGroupV2.builder()
                                .modifier(bacon)
                                .build(),
                        IntegrationModifierInModifierGroupV2.builder()
                                .modifier(cheddar)
                                .build()
                ))
                .max(1).min(0).free(1)
                .build();

        final IntegrationMenuItemV2 benJerry = IntegrationMenuItemV2.builder()
                .id("2394042")
                .code("2394042")
                .title(Map.of("en", "Ben & Jerry Choc Fudge Brownie",
                        "es", "Ben & Jerry Brownie de Chocolate",
                        "fr", "Ben & Jerry Brownie au Chocolat"))
                .description(Map.of("en", "A rich chocolate fudge brownie ice cream.",
                        "es", "Un helado rico...",
                        "fr", "Une glace riche..."))
                .pricesByType(priceAllTypes(new BigDecimal("5.99")))
                .categories(List.of(
                        IntegrationMenuItemInCategoryV2.builder()
                                .category(iceCream500)
                                .sortOrder(2394043)
                                .build()
                ))
                .modifierGroups(List.of())
                .build();

        final IntegrationMenuItemV2 morningMouthful = IntegrationMenuItemV2.builder()
                .id("2029111")
                .code("2029111")
                .title(Map.of("en", "Morning Mouthful", "es", "Bocado Matutino", "fr", "Bouchée Matinale"))
                .description(Map.of("en", "A delicious morning meal.", "es", "Un delicioso desayuno.", "fr", "Un délicieux petit-déjeuner."))
                .pricesByType(priceAllTypes(new BigDecimal("8.00")))
                .categories(List.of(
                        IntegrationMenuItemInCategoryV2.builder()
                                .category(sundaes)
                                .sortOrder(1)
                                .build()
                ))
                .modifierGroups(List.of(
                        IntegrationModifierGroupEmbeddedV2.builder()
                                .basedOn(group1)
                                .max(3)
                                .build(),
                        IntegrationModifierGroupEmbeddedV2.builder()
                                .basedOn(group2)
                                .build()
                ))
                .build();

        return Optional.of(MenuAggregatorV2.builder()
                .categories(List.of(
                        sweets, iceCream500, sundaes
                ))
                .modifierGroups(List.of(group1, group2))
                .modifiers(List.of(cheddar, tomato, bacon))
                .items(List.of(benJerry, morningMouthful))
                .revision(1727191974L)
                .build());
    }

    @Override
    public @Nonnull Optional<OrdersWebhookRequest> webhook(final @Nonnull PosVendorWebhookRequest src) {
        Objects.requireNonNull(src, "webhook body is required");

        final OrdersWebhookRequest dst = new OrdersWebhookRequest();

        final List<Order> orders = src.getOrders().stream().map(patch -> {
            final Order o = new Order();

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

        return Optional.of(dst);
    }

    private @Nonnull Map<OrderingType, Price> priceAllTypes(final @Nonnull BigDecimal price) {
        final PricedEntityV2 p = new PricedEntityV2();
        Arrays.stream(OrderingType.values()).forEach(
                type -> p.addPriceByType(
                        type,
                        Price.builder()
                                .setValue(price)
                        .build()
                )
        );

        return p.getPricesByOrderingType();
    }
}


