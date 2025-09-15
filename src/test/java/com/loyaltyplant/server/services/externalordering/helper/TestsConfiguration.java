package com.loyaltyplant.server.services.externalordering.helper;

import com.loyaltyplant.oss.plantsql.SQLTemplateFactory;
import com.loyaltyplant.server.services.externalordering.service.IExternalPosConvertingService;
import com.loyaltyplant.server.services.externalordering.service.IExternalWebhookForwarder;
import com.loyaltyplant.server.services.externalordering.service.ITokenValidationService;
import com.loyaltyplant.server.services.externalordering.service.impl.PosMockService;
import com.loyaltyplant.server.services.externalordering.service.impl.TokenValidationMockService;
import com.loyaltyplant.server.services.externalordering.service.impl.WebhookForwarder;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@TestConfiguration
@ContextConfiguration
public class TestsConfiguration {
    @Bean(value = {"psql"})
    public SQLTemplateFactory sqlTemplateFactory() {
        return Mockito.mock(SQLTemplateFactory.class);
    }

    @Bean
    public ITokenValidationService tokenValidationService() {
        return new TokenValidationMockService();
    }

    @Bean
    public IExternalPosConvertingService externalPosConvertingService() {
        return new PosMockService();
    }

    @Bean
    public IExternalWebhookForwarder externalWebhookForwarder(ITokenValidationService tokenValidationService) {
        return new WebhookForwarder(null, tokenValidationService, "");
    }
}
