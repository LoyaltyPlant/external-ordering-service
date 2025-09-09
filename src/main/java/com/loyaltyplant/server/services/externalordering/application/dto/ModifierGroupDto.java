package com.loyaltyplant.server.services.externalordering.application.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifierGroupDto {
    private String refId;
    private String id;
    private String code;
    private Map<String, String> title;
    private Map<String, String> description;
    private Integer max;
    private Integer min;
    private Integer free;
}