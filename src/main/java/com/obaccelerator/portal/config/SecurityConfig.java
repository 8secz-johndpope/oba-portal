package com.obaccelerator.portal.config;

import com.obaccelerator.common.error.ExceptionHandlingFilter;
import com.obaccelerator.portal.auth.spring.DummyPreAuthorizedAuthenticationProvider;
import com.obaccelerator.portal.auth.spring.DummyPreAuthenticatedAuthenticationManager;
import com.obaccelerator.portal.auth.spring.PreAuthenticationFilter;
import com.obaccelerator.portal.portaluser.PortalUserService;
import com.obaccelerator.portal.shared.session.SessionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        PreAuthenticationFilter preAuthFilter = new PreAuthenticationFilter(sessionService, portalUserService);
        preAuthFilter.setAuthenticationManager(new DummyPreAuthenticatedAuthenticationManager());

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(new DummyPreAuthorizedAuthenticationProvider())
                .addFilterBefore(new ExceptionHandlingFilter(), CorsFilter.class)
                .addFilterBefore(preAuthFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/pages/**").permitAll() // getting pages allowed
                .antMatchers(HttpMethod.POST, "/sessions/").permitAll() // creating a new session allowed
                .antMatchers(HttpMethod.POST, "/registrations/").permitAll() // creating a new registration allowed
                .anyRequest()
                .authenticated();
    }
}
