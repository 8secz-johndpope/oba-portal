package com.obaccelerator.portal.authentication.spring;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

/**
 * Provides a first line of defense against cross-organization data access. The second line of defense being db queries
 * that enforce checks on the organization id.
 */
public class OrganizationAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {

        // Endpoints opened using the permitAll config in SecurityConfig can be freely accessed by anyone
        if (attributes != null && attributes.stream().anyMatch(a -> a.toString().equals("permitAll"))) {
            return 1;
        }



        // Abstain
        return 0;
    }
}
