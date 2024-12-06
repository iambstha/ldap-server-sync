package org.base.exception;

import jakarta.ws.rs.core.Response;

import java.io.Serial;

public class ClientException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ClientException(String message) {
        super(message, Response.Status.SERVICE_UNAVAILABLE, Response.Status.SERVICE_UNAVAILABLE.getStatusCode());
    }

}
