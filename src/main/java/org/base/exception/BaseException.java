package org.base.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.io.Serial;

@Getter
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Response.Status errorCode;
    private final int httpStatusCode;

    public BaseException(String message, Response.Status errorCode, int httpStatusCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    public BaseException(String message, int httpStatusCode) {
        this(message, Response.Status.INTERNAL_SERVER_ERROR, httpStatusCode);
    }
}
