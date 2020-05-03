package com.obaccelerator.portal;

import com.obaccelerator.portal.config.ObaPortalProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(value = ObaPortalProperties.class)
public class ObaPortalApplication {

    public static final String SESSION_COOKIE_NAME = "oba_portal_session";
    public static final int SESSION_DURATION_MINUTES = 30;
    public static final String SESSION_URL = "/sessions/";
    public static final String ORGANIZATION_ADMIN = "ORGANIZATION_ADMIN";



    public static void main(String[] args) {
        SpringApplication.run(ObaPortalApplication.class, args);
    }
}
