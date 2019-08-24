package com.obaccelerator.portal.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TlsConfig {

    /**
     * OBA portal uses one-way TLS towards the client
     * @param properties
     * @return
     */
    @Bean
    public ServletWebServerFactory servletContainer(ObaPortalProperties properties) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Ssl ssl = new Ssl();
        ssl.setKeyStore(properties.getClientTlsKeyStorePath());
        ssl.setKeyStorePassword(properties.getClientTlsKeystorePassword());
        ssl.setKeyPassword(properties.getClientTlsKeystorePassword());
        ssl.setKeyStoreType("PKCS12");
        ssl.setEnabled(true);
        ssl.setEnabledProtocols(new String[]{"TLSv1.2"});
        ssl.setKeyAlias("oba-portal-client-tls");
        ssl.setProtocol("TLSv1.2");
        ssl.setCiphers(new String[]{"ECDHE-RSA-AES128-GCM-SHA256", "ECDHE-RSA-AES256-SHA384"});
        tomcat.setPort(8445);
        tomcat.setSsl(ssl);
        return tomcat;
    }
}
