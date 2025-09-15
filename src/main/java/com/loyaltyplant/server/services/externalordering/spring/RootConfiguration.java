package com.loyaltyplant.server.services.externalordering.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.loyaltyplant.server.commons.properties.DefaultSpringPropertiesConfiguration;
import com.loyaltyplant.server.commons.logging.spring.LoggingConfiguration;
import com.loyaltyplant.server.services.externalordering.VersionInfo;
import com.loyaltyplant.server.services.externalordering.spring.controller.CommonController;
import com.loyaltyplant.server.services.externalordering.spring.database.DatabaseConfiguration;


import org.springframework.context.annotation.*;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.http.HttpClient;
import java.time.Duration;

import static com.loyaltyplant.server.services.externalordering.utils.Consts.WEBHOOK_TO_LP_CONNECT_TIMEOUT_SECONDS;
import static com.loyaltyplant.server.services.externalordering.utils.Consts.WEBHOOK_TO_LP_REQUEST_READ_TIMEOUT_SECONDS;

@Configuration
@Import({
        DatabaseConfiguration.class,
        LoggingConfiguration.class,
        DefaultSpringPropertiesConfiguration.class,
        ConsulConfiguration.class,
        OpenApiConfiguration.class,
        MicrometerMetrics.class,
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
        return JsonMapper.builder().build().findAndRegisterModules();
    }

    @Bean("webhookRestTemplate")
    public RestTemplate restTemplate() {
        var httpClient = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(WEBHOOK_TO_LP_CONNECT_TIMEOUT_SECONDS))
                        .followRedirects(HttpClient.Redirect.ALWAYS)
                        .build();
        var factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofSeconds(WEBHOOK_TO_LP_REQUEST_READ_TIMEOUT_SECONDS));
        return new RestTemplate(factory);
    }
}
