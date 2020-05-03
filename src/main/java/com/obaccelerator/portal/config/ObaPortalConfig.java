package com.obaccelerator.portal.config;

import com.obaccelerator.common.http.ApacheHttpsClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ObaPortalConfig {

    @Bean
    public HttpClient obaPortalHttpClient(ObaPortalProperties portalProperties) {
        ApacheHttpsClientFactory.HttpsClientFactoryInput clientFactoryInput = ApacheHttpsClientFactory.HttpsClientFactoryInput.builder()
                .connectTimeOut(5000)
                .socketTimeOut(5000)
                .defaultPoolSize(2)
                .maxPoolSize(10)
                .keyStoreClassPath(portalProperties.getBackendTlsKeyStorePath())
                .keyStorePw(portalProperties.getBackendTlsKeyStorePassword())
                .trustStoreClassPath(portalProperties.getBackendTlsTrustStorePath())
                .trustStorePw(portalProperties.getBackendTlsTrustStorePassword())
                .trustSelfSigned(true)
                .build();
        return ApacheHttpsClientFactory.getHttpsClient(clientFactoryInput);
    }


}
