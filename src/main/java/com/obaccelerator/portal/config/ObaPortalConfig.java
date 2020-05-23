package com.obaccelerator.portal.config;

import com.obaccelerator.common.http.ApacheHttpsClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ObaPortalConfig {

    /**
     * The HTTP client connecting to OBA must accept OBA's self-signed cert
     * <p>
     * TODO: security : trust certs signed by the yet to be created OBA CA
     *
     */
    @Bean
    public HttpClient obaPortalHttpClient() {
        ApacheHttpsClientFactory.HttpsClientFactoryInput clientFactoryInput = ApacheHttpsClientFactory.HttpsClientFactoryInput.builder()
                .connectTimeOut(5000)
                .socketTimeOut(5000)
                .defaultPoolSize(2)
                .maxPoolSize(10)
                .trustSelfSigned(true)
                .build();
        return ApacheHttpsClientFactory.getHttpsClient(clientFactoryInput);
    }
}