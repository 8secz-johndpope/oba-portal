package com.obaccelerator.portal.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * HTTP for now. It is behind nginx which does HTTPS
 */
//@Configuration
public class TlsConfig {

    /**
     * Specifies how the server treats clients. There is a server keystore that holds a server certificate, but
     * no trust store, because there is a frontend / web browser connecting, no some other system.
     *
     * OBA portal uses one-way TLS towards the client
     * @param properties
     * @return
     */
    @Bean
    public ServletWebServerFactory servletContainer(ObaPortalProperties properties) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Ssl ssl = new Ssl();
        String keyStoreFileSystemPath = getClass().getResource(properties.getClientTlsKeyStorePath()).getPath();
        ssl.setKeyStore(keyStoreFileSystemPath);
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
