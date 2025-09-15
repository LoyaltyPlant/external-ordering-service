package com.loyaltyplant.server.services.externalordering.service.impl;

import com.loyaltyplant.server.commons.vault.SecretProvider;
import com.loyaltyplant.server.services.externalordering.service.IVaultStorageService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Log4j2
public class VaultStorageService implements IVaultStorageService {
    private final SecretProvider secretProvider;

    @Autowired
    public VaultStorageService(final ObjectProvider<SecretProvider> secretProvider) {
        this.secretProvider = secretProvider.getIfAvailable();
    }

    @Override
    public @Nullable Map<String, String> readKey(final @Nonnull String path) {
        Objects.requireNonNull(path, "path must not be null");

        if (secretProvider == null) {
            LOGGER.error("Secret provider is not available");
            return null;
        }

        return secretProvider.read(path);
    }
}
