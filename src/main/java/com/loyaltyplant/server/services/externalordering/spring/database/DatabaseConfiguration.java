package com.loyaltyplant.server.services.externalordering.spring.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Database configuration for the application.
 *
 * Note: If your service doesn't require database support, you can safely remove:
 * 1. This configuration class
 * 2. The LiquibaseConfiguration class
 * 3. The 'lpcommons-db-migration' dependency from the POM file
 * 4. Any other database-related configurations and dependencies
 */
@Configuration
@Import({
        PostgresDatabaseConfiguration.class,
        LiquibaseConfiguration.class
})
public class DatabaseConfiguration {
}