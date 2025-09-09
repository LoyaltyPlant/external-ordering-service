package com.loyaltyplant.server.services.externalordering.application.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private String refId;
    private String id;
    private String code;
    private Map<String, String> title;
    private Map<String, String> description;
    private Map<String, PriceDto> pricesByOrderingType;
    private List<CategoryLinkDto> categories;
    private List<String> modifierGroups;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryLinkDto {
        private String category;
        private Integer sortOrder;
    }
}