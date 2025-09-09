package com.loyaltyplant.server.services.externalordering.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
    private List<CategoryDto> categories;
    private List<ModifierGroupDto> modifierGroups;
    private List<ModifierDto> modifiers;
    private List<ItemDto> items;
    private long revision;
}