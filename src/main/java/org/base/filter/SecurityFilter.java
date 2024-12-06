package org.base.filter;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION + 1)
@ApplicationScoped
public class SecurityFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @ServerRequestFilter
    public void onRequest(ContainerRequestContext requestContext) throws IOException {
        logger.info("Server request filter triggered . . . \n");
    }

    @ServerResponseFilter
    public void onResponse(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        logger.info("Server response filter triggered . . . \n");
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        logger.info("Security filter started . . . \n");

        // some filter performed here

        logger.info("Security filter ended . . . \n");
    }

}
