package org.base.filter;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.ext.Provider;
import org.base.domain.CustomObjectMapper;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Provider
public class ApiFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiFilter.class);

    @Inject
    CustomObjectMapper customObjectMapper;

    @ServerRequestFilter
    public void onRequest(ContainerRequestContext requestContext) throws IOException {
        logger.info("API triggered.");
        String method = requestContext.getMethod();
        String uri = requestContext.getUriInfo().getRequestUri().toString();
        logger.info("API request: {} -> {}\n", method, uri);
    }

    @ServerResponseFilter
    public void onResponse(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        try {
            if (responseContext.hasEntity()) {
                Object entity = responseContext.getEntity();
                String responseBody;
                if (entity != null) {
                    responseBody = customObjectMapper.writeValueAsString(entity);
                    logger.info("Response Body: {}", responseBody);
                } else {
                    logger.info("Response entity is null.");
                }
            } else {
                logger.info("Response has no entity.");
            }
        } catch (Exception e) {
            logger.error("Error serializing response body: {}", e.getMessage());
        }
        logger.info("API call completed.\n");
    }


    public String getResponseBody(ContainerResponseContext responseContext) throws IOException {
        Object entity = responseContext.getEntity();
        String responseBody;
        if (entity != null) {
            return customObjectMapper.writeValueAsString(entity);
        }
        return null;
    }

}
