package com.obaccelerator.portal.config;

import com.obaccelerator.portal.authentication.cognito.CognitoService;
import com.obaccelerator.portal.authentication.spring.SpringSessionAuthenticationFilter;
import com.obaccelerator.portal.portaluser.PortalUserService;
import com.obaccelerator.portal.session.FirstTimeSessionService;
import com.obaccelerator.portal.shared.session.SessionService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SessionService sessionService;
    private final CognitoService cognitoService;
    private final PortalUserService portalUserService;
    private final FirstTimeSessionService firstTimeSessionService;

    public SecurityConfig(SessionService sessionService, CognitoService cognitoService, PortalUserService portalUserService, FirstTimeSessionService firstTimeSessionService) {
        this.sessionService = sessionService;
        this.cognitoService = cognitoService;
        this.portalUserService = portalUserService;
        this.firstTimeSessionService = firstTimeSessionService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
/*
        This may work :
        - Create a OncePerRequestFilter that is limited to  a particular URL using the FilterRegistrationBean for initial authentication
                - Use a second OncePerRequestFilter that fires always for per-request authentication

            In both cases use SecurityContextHolder.getContext().setAuthentication() to set the authentication, either ORGANIZATION_ADMIN OR ANONYMOUS

                AbstractPreAuthenticatedProcessingFilter cannot be tied to a specific URL. I don't see why I would use this for per-request authentication
        AbstractAuthenticationProcessingFilter can be tied to a URL, but insists we redirect somewhere after authentication. It doesn't seem to be intended for REST
*/

/*
        ObaCognitoAuthFilter obaAuthFilter = new ObaCognitoAuthFilter(SESSION_URL, cognitoService, portalUserService, firstTimeSessionService, sessionService);
        obaAuthFilter.setAllowSessionCreation(false);
        obaAuthFilter.setAuthenticationDetailsSource(new ObaAuthenticationDetailsSource(portalUserService, cognitoService));
        obaAuthFilter.setAuthenticationSuccessHandler(new ForwardAuthenticationSuccessHandler(SESSION_URL));
        obaAuthFilter.setAuthenticationManager(new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                return null;
            }
        });
*/

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAfter(new SpringSessionAuthenticationFilter(sessionService, portalUserService), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/pages/**", "/sessions/").permitAll()
                .anyRequest()
                .authenticated();
    }

    @Bean
    public FilterRegistrationBean<SpringSessionAuthenticationFilter> logFilter() {
        FilterRegistrationBean<SpringSessionAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SpringSessionAuthenticationFilter(sessionService, portalUserService));
        registrationBean.addUrlPatterns("/health", "/faq/*");

        return registrationBean;
    }


}
