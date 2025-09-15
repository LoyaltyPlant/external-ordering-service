package com.loyaltyplant.server.services.externalordering.helper;

import com.loyaltyplant.oss.plantsql.SQLTemplateFactory;
import com.loyaltyplant.server.services.externalordering.service.IExternalPosConvertingService;
import com.loyaltyplant.server.services.externalordering.service.IExternalWebhookForwarder;
import com.loyaltyplant.server.services.externalordering.service.ITokenValidationService;
import com.loyaltyplant.server.services.externalordering.service.impl.PosMockService;
import com.loyaltyplant.server.services.externalordering.service.impl.TokenValidationMockService;
import com.loyaltyplant.server.services.externalordering.service.impl.WebhookForwarder;
import io.micrometer.core.instrument.MeterRegistry;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class TestsConfiguration {
    @Bean(value = {"psql"})
    @Primary
    public SQLTemplateFactory sqlTemplateFactory() {
        return Mockito.mock(SQLTemplateFactory.class);
    }

    @Bean
    @Primary
    public ITokenValidationService tokenValidationService() {
        return new TokenValidationMockService();
    }

    @Bean
    @Primary
    public IExternalPosConvertingService externalPosConvertingService() {
        return new PosMockService(Mockito.mock(MeterRegistry.class));
    }

    @Bean
    @Primary
    public IExternalWebhookForwarder externalWebhookForwarder(final ITokenValidationService tokenValidationService) {
        return new WebhookForwarder(Mockito.mock(RestTemplate.class), tokenValidationService, "https://example.com");
    }
}
