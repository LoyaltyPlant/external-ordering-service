package com.loyaltyplant.server.services.externalordering.service.impl;

import com.loyaltyplant.server.services.externalordering.service.ITokenValidationService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

@Service
public class TokenValidationMockService implements ITokenValidationService {
    @Override
    public boolean isRequestTokenValid(final @Nonnull Integer salesOutletId, final @Nonnull String token) {
        return true;
    }

    @Override
    public @Nonnull String getResponseToken(final @Nonnull Integer salesOutletId) {
        return "responseToken";
    }
}
