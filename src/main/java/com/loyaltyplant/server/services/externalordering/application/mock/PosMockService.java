package com.loyaltyplant.server.services.externalordering.application.mock;

import com.loyaltyplant.common.integration.protocol.digitalordering.order.Order;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.OrderState;
import com.loyaltyplant.common.integration.protocol.digitalordering.order.OrderStatus;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.CreatePosOrderRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.request.GetPosOrdersRequest;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.CreatePosOrderResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.GetPosOrdersResponse;
import com.loyaltyplant.common.integration.protocol.digitalordering.response.HealthcheckResponse;
import com.loyaltyplant.server.services.externalordering.application.dto.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PosMockService {

    public static final String AUTH_TOKEN_HEADER = "AuthorizationToken";

    public HealthcheckResponse healthcheck(Integer salesOutletId, String authToken) {
        HealthcheckResponse resp = new HealthcheckResponse();
        resp.setServices(Map.of(
                "POS", "UP",
                "DB", "UP",
                "pos-" + salesOutletId, "UP"
        ));
        return resp;
    }

    public GetPosOrdersResponse getOrders(Integer salesOutletId, String authToken, GetPosOrdersRequest request) {
        List<Order> orders = new ArrayList<>();

        if (request != null && request.getOrderPosIds() != null) {
            for (String orderPosId : request.getOrderPosIds()) {
                Order order = Order.builder()
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

        return new GetPosOrdersResponse(orders);
    }

    public String mockResponseToken(String incoming) {
        return incoming == null ? "mock-response-token" : "resp-" + incoming;
    }

    public CreatePosOrderResponse createOrder(Integer salesOutletId, String authToken, CreatePosOrderRequest request) {
        String generatedPosId = "POS-" + UUID.randomUUID();
        return new CreatePosOrderResponse(generatedPosId);
    }

    public MenuEnvelope getMenu(Integer salesOutletId, String authToken) {
        String catRootSweets = "abb26553-5591-eedb-02bc-6debe1ce9bf2";
        String catIceCream500 = "53bb0d8d-a898-121c-3037-9496425af64a";
        String catSundaes = "130788e0-c6e8-c6c5-8a02-2315e08a15e6";

        CategoryDto sweets = CategoryDto.builder()
                .refId(catRootSweets).id("2029027").code("2029027")
                .title(Map.of("en", "Sweets", "es", "Dulces", "fr", "Desserts"))
                .description(Map.of("en", "A variety of sweet treats.", "es", "Una variedad de dulces.", "fr", "Une variété de desserts sucrés."))
                .root(true).rootSortOrder(3)
                .subCategories(List.of(
                        new CategoryDto.SubCategoryLinkDto(catIceCream500, 4),
                        new CategoryDto.SubCategoryLinkDto(catSundaes, 5)
                ))
                .build();

        CategoryDto iceCream500 = CategoryDto.builder()
                .refId(catIceCream500).id("2029171").code("2029171")
                .title(Map.of("en", "Ice Cream 500 ml", "es", "Helado 500 ml", "fr", "Glace 500 ml"))
                .description(Map.of("en", "A 500 ml serving of ice cream.", "es", "Una porción de helado de 500 ml.", "fr", "Une portion de glace de 500 ml."))
                .root(false).rootSortOrder(25)
                .subCategories(List.of())
                .build();

        CategoryDto sundaes = CategoryDto.builder()
                .refId(catSundaes).id("2029182").code("2029182")
                .title(Map.of("en", "Ice Cream Sundaes", "es", "Helados Sundaes", "fr", "Coupes glacées"))
                .description(Map.of("en", "A delicious ice cream dessert with various toppings.", "es", "Un delicioso postre de helado...", "fr", "Un délicieux dessert glacé..."))
                .root(false).rootSortOrder(27)
                .subCategories(List.of())
                .build();

        ModifierDto cheddar = ModifierDto.builder()
                .refId("92a4258e-44f4-ce44-1118-30e46ee0947c")
                .id("2029334").code("2029334")
                .title(Map.of("en", "cheddar cheese", "es", "queso cheddar", "fr", "fromage cheddar"))
                .description(Map.of("en", "A slice of cheddar cheese.", "es", "Una rebanada...", "fr", "Une tranche..."))
                .pricesByOrderingType(freePriceAllTypes())
                .build();

        ModifierDto tomato = ModifierDto.builder()
                .refId("9a13d0f1-09d6-abe3-ff21-86c963c43be3")
                .id("2029337").code("2029337")
                .title(Map.of("en", "tomato sauce", "es", "salsa de tomate", "fr", "sauce tomate"))
                .description(Map.of("en", "A serving of tomato sauce.", "es", "Una porción...", "fr", "Une portion..."))
                .pricesByOrderingType(freePriceAllTypes())
                .build();

        ModifierDto bacon = ModifierDto.builder()
                .refId("1bfdd230-e348-2b0a-9a58-d0fd29f9bbf3")
                .id("2029512").code("2029512")
                .title(Map.of("en", "bacon", "es", "tocino", "fr", "bacon"))
                .description(Map.of("en", "A slice of crispy bacon.", "es", "Una rebanada...", "fr", "Une tranche..."))
                .pricesByOrderingType(priceAllTypes(new BigDecimal("0.85")))
                .build();

        ModifierGroupDto group1 = ModifierGroupDto.builder()
                .refId("7bb1a809-094d-fff9-7526-96b37d5e9931")
                .id("ing_2029111_0_0").code("ing_2029111_0_0")
                .title(Map.of("en", "Ingredients for Morning Mouthful",
                        "es", "Ingredientes para el Bocado Matutino",
                        "fr", "Ingrédients pour la Bouchée Matinale"))
                .description(Map.of("en", "Choose your ingredients...", "es", "Elige tus ingredientes...", "fr", "Choisissez vos ingrédients..."))
                .max(6).min(0).free(6)
                .build();

        ModifierGroupDto group2 = ModifierGroupDto.builder()
                .refId("826948c9-accd-64c7-6804-b897afcff64d")
                .id("ing_2029111_0.85_0").code("ing_2029111_0.85_0")
                .title(Map.of("en", "Ingredients for Morning Mouthful",
                        "es", "Ingredientes para el Bocado Matutino",
                        "fr", "Ingrédients pour la Bouchée Matinale"))
                .description(Map.of("en", "Additional ingredients...", "es", "Ingredientes adicionales...", "fr", "Ingrédients supplémentaires..."))
                .max(1).min(0).free(1)
                .build();

        ItemDto benJerry = ItemDto.builder()
                .refId("9ed13e20-f6c5-400f-6ee3-e2707bc22dfa")
                .id("2394042").code("2394042")
                .title(Map.of("en", "Ben & Jerry Choc Fudge Brownie",
                        "es", "Ben & Jerry Brownie de Chocolate",
                        "fr", "Ben & Jerry Brownie au Chocolat"))
                .description(Map.of("en", "A rich chocolate fudge brownie ice cream.",
                        "es", "Un helado rico...",
                        "fr", "Une glace riche..."))
                .pricesByOrderingType(priceAllTypes(new BigDecimal("5.99")))
                .categories(List.of(new ItemDto.CategoryLinkDto(catIceCream500, 2394043)))
                .modifierGroups(List.of())
                .build();

        ItemDto morningMouthful = ItemDto.builder()
                .refId("68c81eee-4471-77a3-e553-4c775a561b73")
                .id("2029111").code("2029111")
                .title(Map.of("en", "Morning Mouthful", "es", "Bocado Matutino", "fr", "Bouchée Matinale"))
                .description(Map.of("en", "A delicious morning meal.", "es", "Un delicioso desayuno.", "fr", "Un délicieux petit-déjeuner."))
                .pricesByOrderingType(priceAllTypes(new BigDecimal("8.00")))
                .categories(List.of(new ItemDto.CategoryLinkDto(catSundaes, 1)))
                .modifierGroups(List.of(
                        "7bb1a809-094d-fff9-7526-96b37d5e9931",
                        "826948c9-accd-64c7-6804-b897afcff64d"
                ))
                .build();

        MenuDto menu = MenuDto.builder()
                .categories(List.of(
                        sweets, iceCream500, sundaes
                ))
                .modifierGroups(List.of(group1, group2))
                .modifiers(List.of(cheddar, tomato, bacon))
                .items(List.of(benJerry, morningMouthful))
                .revision(1727191974L)
                .build();

        return new MenuEnvelope(menu);
    }

    private Map<String, PriceDto> freePriceAllTypes() {
        PriceDto p = PriceDto.builder()
                .value(BigDecimal.ZERO)
                .taxes(Collections.emptyList())
                .build();
        return Map.of("PICKUP", p, "EAT_IN", p, "DELIVERY", p);
    }

    private Map<String, PriceDto> priceAllTypes(BigDecimal value) {
        PriceDto p = PriceDto.builder()
                .value(value)
                .taxes(Collections.emptyList())
                .build();
        return Map.of("PICKUP", p, "EAT_IN", p, "DELIVERY", p);
    }
}


