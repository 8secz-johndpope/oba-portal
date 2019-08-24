package com.obaccelerator.portal.config;

import com.obaccelerator.common.http.ApacheHttpsClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
public class ObaPortalConfig {

    private Environment environment;

    public ObaPortalConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public HttpClient obaPortalHttpClient(ObaPortalProperties portalProperties) {
        if(environment.getActiveProfiles().length == 0) {
            throw new RuntimeException("Environment not set");
        }

        log.info("Creating OBA Portal HTTP client for environment {}", environment.getActiveProfiles()[0]);
        ApacheHttpsClientFactory.HttpsClientFactoryInput clientFactoryInput = ApacheHttpsClientFactory.HttpsClientFactoryInput.builder()
                .connectTimeOut(5000)
                .socketTimeOut(5000)
                .defaultPoolSize(2)
                .maxPoolSize(10)
                .keyStoreClassPath(portalProperties.getBackendTlsKeyStorePath())
                .keyStorePw(portalProperties.getBackendTlsKeyStorePassword())
                .trustStoreClassPath(portalProperties.getBackendTlsKeyStorePath())
                .trustStorePw(portalProperties.getBackendTlsTrustStorePath())
                .trustSelfSigned(true)
                .build();
        return ApacheHttpsClientFactory.getHttpsClient(clientFactoryInput);
    }
}
