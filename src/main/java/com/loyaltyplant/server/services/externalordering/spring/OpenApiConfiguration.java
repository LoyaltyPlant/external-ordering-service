package com.loyaltyplant.server.services.externalordering.spring;

import com.loyaltyplant.server.services.externalordering.VersionInfo;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public GroupedOpenApi swaggerDocket() {
        return GroupedOpenApi.builder()
                .group("external-ordering-service")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("external-ordering-service")
                                .description("Template microservice for integrating external POS systems with the Digital Ordering API. " +
                                        "Includes REST endpoints for healthcheck, orders, menu, and webhooks.")
                                .version(VersionInfo.Build.VERSION)
                                .contact(
                                        new Contact()
                                                .name("LP")
                                                .url("https://git.loyaltyplant.com/server/external-ordering-service")
                                )
                );
    }
}