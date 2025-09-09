package com.loyaltyplant.server.services.externalordering.application.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private String refId;
    private String id;
    private String code;
    private Map<String, String> title;
    private Map<String, String> description;
    private boolean root;
    private Integer rootSortOrder;
    private List<SubCategoryLinkDto> subCategories;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class SubCategoryLinkDto {
        private String subCategory;
        private Integer sortOrder;
    }
}