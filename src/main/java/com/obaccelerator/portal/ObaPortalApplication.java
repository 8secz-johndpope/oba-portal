package com.obaccelerator.portal;

import com.obaccelerator.portal.config.ObaPortalProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 *  Excluding ErrorMvcAutoConfiguration in order to prevent Spring from using the BasicErrorControlller for handling errors
 *  Oba-portal handles exceptions using
 *  - ControllerAdvice for application errors
 *  - ObaGlobalExceptionFilter for filter errors
 *  - ObaPortalAuthenticationEntryPoint for AuthenticationExceptions
 */
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableAsync
@EnableConfigurationProperties(value = ObaPortalProperties.class)
public class ObaPortalApplication {

    public static final String SESSION_COOKIE_NAME = "oba_portal_session";
    public static final int SESSION_DURATION_MINUTES = 30;
    public static final String ORGANIZATION_ADMIN = "ORGANIZATION_ADMIN";

    public static void main(String[] args) {
        SpringApplication.run(ObaPortalApplication.class, args);
    }
}
