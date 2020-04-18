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

    public static void main(String[] args) {
        SpringApplication.run(ObaPortalApplication.class, args);
    }
}
