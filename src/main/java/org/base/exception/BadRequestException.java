package org.base.exception;

import jakarta.ws.rs.core.Response;

import java.io.Serial;

public class BadRequestException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(message, Response.Status.BAD_REQUEST, Response.Status.BAD_REQUEST.getStatusCode());
    }

}
