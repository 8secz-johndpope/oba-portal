package com.obaccelerator.portal.config;

import com.obaccelerator.common.error.ExceptionHandlingFilter;
import com.obaccelerator.portal.auth.spring.CookiePreAuthenticationFilter;
import com.obaccelerator.portal.auth.spring.ObaPortalAuthenticationEntryPoint;
import com.obaccelerator.portal.auth.spring.PortalPreAuthenticatedAuthenticationManager;
import com.obaccelerator.portal.auth.spring.PortalPreAuthorizedAuthenticationProvider;
import com.obaccelerator.portal.portaluser.PortalUserService;
import com.obaccelerator.portal.shared.session.SessionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SessionService sessionService;
    private final PortalUserService portalUserService;

    public SecurityConfig(SessionService sessionService, PortalUserService portalUserService) {
        this.sessionService = sessionService;
        this.portalUserService = portalUserService;
    }

    /**
     * Don't use antMatchers on the http object! It doesn't mix with method level security (@PreAuth) annotations.
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CookiePreAuthenticationFilter preAuthFilter = new CookiePreAuthenticationFilter(sessionService, portalUserService);
        preAuthFilter.setAuthenticationManager(new PortalPreAuthenticatedAuthenticationManager());
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new ExceptionHandlingFilter(), CorsFilter.class)
                .addFilterBefore(preAuthFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new ObaPortalAuthenticationEntryPoint());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new PortalPreAuthorizedAuthenticationProvider());
    }

}
