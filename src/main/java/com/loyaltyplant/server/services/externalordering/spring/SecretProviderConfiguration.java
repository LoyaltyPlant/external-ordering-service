package com.loyaltyplant.server.services.externalordering.spring;

import com.loyaltyplant.server.commons.healthcheck.HealthChecks;
import com.loyaltyplant.server.commons.vault.BootstrapSecretProviderFactory;
import com.loyaltyplant.server.commons.vault.SecretProvider;
import com.loyaltyplant.server.commons.vault.VaultHealthCheck;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

@Log4j2
@Configuration
public class SecretProviderConfiguration {
    @Bean
    @Lazy
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public SecretProvider secretProvider(ApplicationContext applicationContext,
                                         Optional<HealthChecks> healthChecks) throws CredentialNotFoundException {
        try {
            SecretProvider secretProvider = BootstrapSecretProviderFactory.buildVault(applicationContext);
            healthChecks.ifPresent(hc -> hc.register(new VaultHealthCheck(secretProvider)));
            return secretProvider;
        } catch (Exception e) {
            LOGGER.error("Failed to create secret provider", e);
            return null;
        }
    }

    @Bean
    public static BeanFactoryPostProcessor vaultOperationsExecutorCreator() {
        return beanFactory -> {
            final String[] allExecutors = beanFactory.getBeanNamesForType(ScheduledExecutorService.class);
            if (allExecutors.length == 0) {
                final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
                scheduler.setPoolSize(1);
                scheduler.initialize();
                beanFactory.registerSingleton(BootstrapSecretProviderFactory.VAULT_OPERATION_EXECUTOR, scheduler.getScheduledExecutor());
            } else if (!beanFactory.containsBean(BootstrapSecretProviderFactory.VAULT_OPERATION_EXECUTOR)) {
                beanFactory.registerAlias(allExecutors[0], BootstrapSecretProviderFactory.VAULT_OPERATION_EXECUTOR);
            }
        };
    }

}