package com.loyaltyplant.server.services.externalordering.service;

import jakarta.annotation.Nonnull;

/**
 * Service interface for token validation and generation.
 * Implementations of this interface are responsible for validating incoming request tokens
 * and generating response tokens for outgoing responses.
 */
public interface ITokenValidationService {

    /**
     * Validates if the provided token is valid for the given sales outlet.
     *
     * @param salesOutletId The unique identifier of the sales outlet making the request.
     *                     Must not be null.
     * @param token The authentication token to validate.
     *             Must not be null.
     * @return true if the token is valid for the specified sales outlet, false otherwise.
     * @throws NullPointerException if either salesOutletId or token is null.
     */
    boolean isRequestTokenValid(@Nonnull Integer salesOutletId, @Nonnull String token);

    /**
     * Generates a response token for the given sales outlet.
     * This token will be included in the response headers for subsequent requests.
     *
     * @param salesOutletId The unique identifier of the sales outlet to generate the token for.
     *                     Must not be null.
     * @return A string representing the generated response token.
     * @throws NullPointerException if salesOutletId is null.
     */
    @Nonnull String getResponseToken(@Nonnull Integer salesOutletId);
}
