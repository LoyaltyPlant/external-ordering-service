package com.loyaltyplant.server.services.externalordering.spring;

import com.loyaltyplant.server.commons.consul.service.ConsulServiceRegistrationConfiguration;
import com.loyaltyplant.server.commons.consul.service.ConsulServiceTags;
import com.loyaltyplant.server.commons.properties.EnvironmentDetector;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ ConsulServiceRegistrationConfiguration.class})
public class ConsulConfiguration {

    @Bean
    public ConsulServiceTags consulServiceTags(final ApplicationContext applicationContext) {
        return ConsulServiceTags.of(
                EnvironmentDetector.detectEnvironment(applicationContext)
        );
    }
}
