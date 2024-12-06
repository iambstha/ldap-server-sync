package org.base.filter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MultivaluedMap;
import org.base.domain.CustomObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@ApplicationScoped
public class RestClientFilter implements ClientRequestFilter, ClientResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(RestClientFilter.class);

    @Inject
    CustomObjectMapper customObjectMapper;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        logger.info("External API call started.");
        logger.info("Request Method: {}", requestContext.getMethod());
        logger.info("Request URL: {}", requestContext.getUri());

        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        logger.info("Request Headers:");
        headers.forEach((key, value) -> logger.info("{}: {}", key, value));

        if (requestContext.getEntity() instanceof Form form) {
            var formData = form.asMap();
            logger.info("Request Form Data:");
            formData.forEach((key, value) -> logger.info("{}: {}", key, value));
        }

        if (requestContext.hasEntity()) {
            Object entity = requestContext.getEntity();
            String requestBody = customObjectMapper.writeValueAsString(entity);
            logger.info("Request Body: {}\n", requestBody);
        }
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        logger.info("Response Status: {}", responseContext.getStatus());
        logger.info("Response Headers: {}", responseContext.getHeaders());

        if (responseContext.hasEntity()) {
            InputStream entityStream = responseContext.getEntityStream();
            String responseBody = new BufferedReader(new InputStreamReader(entityStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            logger.info("Response Body: {}", responseBody);

            responseContext.setEntityStream(new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8)));
            logger.info("External API call ended.\n");
        }
    }
}
