package com.loyaltyplant.server.services.externalordering.service;

import jakarta.annotation.Nullable;
import jakarta.annotation.Nonnull;

import java.util.Map;

/**
 * Service interface for interacting with a secure vault storage system.
 * Provides methods to securely retrieve sensitive configuration data and secrets
 * from a vault storage solution.
 *
 * <p>Implementations of this interface handle the underlying communication with
 * the vault service and provide a clean abstraction for accessing stored secrets.</p>
 *
 * For using this service, you need to have a valid LP vault configuration:
 * you need to have LP_VAULT_URL, LP_VAULT_ROLEID, LP_VAULT_SECRETID in your environment variables.
 *
 * @see com.loyaltyplant.server.services.externalordering.service.impl.VaultStorageService
 */
public interface IVaultStorageService {
    /**
     * Reads a secret value from the vault at the specified path.
     *
     * @param path The path to the secret in the vault. Must not be null.
     *            The path format depends on the underlying vault implementation.
     *            Example: "app/external-ordering-service/credentials"
     * @return A Map containing key-value pairs of the secret data if found,
     *         or null if the specified path does not exist or an error occurs.
     *         The returned map may be empty if the secret exists but contains no data.
     * @throws NullPointerException if the provided path is null or empty.
     */
    @Nullable Map<String, String> readKey(@Nonnull String path);
}
