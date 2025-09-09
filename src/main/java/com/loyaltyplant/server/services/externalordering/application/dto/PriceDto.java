package com.loyaltyplant.server.services.externalordering.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceDto {
    private BigDecimal value;
    private List<BigDecimal> taxes;
}
