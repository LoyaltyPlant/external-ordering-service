package com.loyaltyplant.server.services.externalordering.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyaltyplant.server.commons.properties.DefaultSpringPropertiesConfiguration;
import com.loyaltyplant.server.commons.logging.spring.LoggingConfiguration;
import com.loyaltyplant.server.services.externalordering.VersionInfo;
import com.loyaltyplant.server.services.externalordering.spring.controller.CommonController;
import com.loyaltyplant.server.services.externalordering.spring.database.DatabaseConfiguration;


import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import({
        DatabaseConfiguration.class,
        LoggingConfiguration.class,
        DefaultSpringPropertiesConfiguration.class,
        ConsulConfiguration.class,
        CommonController.class
})
@ComponentScans({
        @ComponentScan(basePackages = "com.loyaltyplant.server.services.externalordering.controller"),
        @ComponentScan(basePackages = "com.loyaltyplant.server.services.externalordering.service"),
        @ComponentScan(basePackages = "com.loyaltyplant.server.services.externalordering.repository")
})
public class RootConfiguration {

    @Bean
    public com.loyaltyplant.server.commons.utils.version.VersionInfo versionInfo() {
        return VersionInfo.INSTANCE;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
