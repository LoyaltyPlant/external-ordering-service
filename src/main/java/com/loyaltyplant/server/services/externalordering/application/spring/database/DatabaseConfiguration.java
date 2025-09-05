package com.loyaltyplant.server.services.externalordering.application.spring.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MysqlDatabaseConfiguration.class,
        PostgresDatabaseConfiguration.class,
        LiquibaseConfiguration.class
})
public class DatabaseConfiguration {
}