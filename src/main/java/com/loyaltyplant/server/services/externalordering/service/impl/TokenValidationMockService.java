package com.loyaltyplant.server.services.externalordering.service.impl;

import com.loyaltyplant.server.services.externalordering.service.ITokenValidationService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class TokenValidationMockService implements ITokenValidationService {
    @Override
    public boolean isRequestTokenValid(final @NotNull Integer salesOutletId, final @NotNull String token) {
        return true;
    }

    @Override
    @NotNull
    public String getResponseToken(final @NotNull Integer salesOutletId) {
        return "responseToken";
    }
}
