package org.base.exception;

import jakarta.ws.rs.core.Response;

import java.io.Serial;

public class UnauthorizedAccessException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnauthorizedAccessException(String message) {
        super(message, Response.Status.UNAUTHORIZED, Response.Status.UNAUTHORIZED.getStatusCode());
    }

}
