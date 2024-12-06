package org.base.exception;

import jakarta.ws.rs.core.Response;

import java.io.Serial;

public class ResourceAlreadyExistsException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExistsException(String message) {
        super(message, Response.Status.NOT_ACCEPTABLE, Response.Status.NOT_ACCEPTABLE.getStatusCode());
    }

}
