package org.base.exception;

import jakarta.ws.rs.core.Response;

import java.io.Serial;

public class ResourceNotFoundException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND, Response.Status.NOT_FOUND.getStatusCode());
    }
}
