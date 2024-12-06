package org.base.exception;

import jakarta.ws.rs.core.Response;

import java.io.Serial;

public class IllegalArgumentException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public IllegalArgumentException(String message) {
        super(message, Response.Status.EXPECTATION_FAILED, Response.Status.EXPECTATION_FAILED.getStatusCode());
    }

}
