package com.obaccelerator.portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Add CORS configuration. It seems to be no problem to list allowed origins for each environment (localhost,
     * dev, prod) as Spring *seems* to respond only with the relevant allowed origin in the Access-Control-Allow-Origin
     * header, being the value that was sent by the client in the Origin header. So, local and dev details would
     * not be exposed on production.
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
