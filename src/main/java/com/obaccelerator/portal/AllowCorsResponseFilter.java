package com.obaccelerator.portal;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Add the Access-Control-Allow-Origin response header in order to allow CORS between the portal frontend and backend
 */

public class AllowCorsResponseFilter implements Filter {

    private static List<String> ALLOWED_HOSTS = Arrays.asList("http://localhost:4200");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        CorsRequestWrapper requestWrapper = new CorsRequestWrapper((HttpServletRequest) servletRequest);

        String origin = requestWrapper.getHeader("Origin");
        if(origin == null) {
            ((HttpServletResponse) servletResponse).sendError(401, "No origin header sent");
        }

        Optional<String> allowedHost = ALLOWED_HOSTS.stream().filter(host -> host.equals(origin.trim())).findFirst();

        if(!allowedHost.isPresent()) {
            ((HttpServletResponse) servletResponse).sendError(401, "Host not allowed");
        }


        CorsResponseWrapper corsResponseWrapper = new CorsResponseWrapper((HttpServletResponse) servletResponse);
        Collection<String> headerNames = corsResponseWrapper.getHeaderNames();
        corsResponseWrapper.addHeader("Access-Control-Allow-Origin", origin);

        filterChain.doFilter(servletRequest, servletResponse);
    }


    private static class CorsResponseWrapper extends HttpServletResponseWrapper {
        public CorsResponseWrapper(HttpServletResponse response) {
            super(response);
        }
    }

    private static class CorsRequestWrapper extends HttpServletRequestWrapper {
        public CorsRequestWrapper(HttpServletRequest request) {
            super(request);
        }
    }
}
