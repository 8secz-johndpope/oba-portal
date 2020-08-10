package com.obaccelerator.portal.config;


import com.obaccelerator.common.error.ObaGlobalExceptionFilter;
import com.obaccelerator.portal.auth.spring.PortalUserArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    final PortalUserArgumentResolver portalUserArgumentResolver;

    public MvcConfig(PortalUserArgumentResolver portalUserArgumentResolver) {
        this.portalUserArgumentResolver = portalUserArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(portalUserArgumentResolver);
    }

    @Bean
    public Filter obaGlobalExceptionHandlingFilter() {
        return new ObaGlobalExceptionFilter();
    }


}
