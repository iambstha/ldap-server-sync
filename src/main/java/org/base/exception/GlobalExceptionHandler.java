package org.base.exception;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.base.domain.ErrorResponse;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();

        if (e instanceof ResourceNotFoundException) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage(), Response.Status.NOT_FOUND);
        } else if (e instanceof BadRequestException) {
            return buildErrorResponse(Response.Status.BAD_REQUEST, e.getMessage(), Response.Status.BAD_REQUEST);
        } else if (e instanceof UnauthorizedAccessException) {
            return buildErrorResponse(Response.Status.UNAUTHORIZED, e.getMessage(), Response.Status.UNAUTHORIZED);
        } else if (e instanceof ResourceAlreadyExistsException) {
            return buildErrorResponse(Response.Status.NOT_ACCEPTABLE, e.getMessage(), Response.Status.NOT_ACCEPTABLE);
        } else if (e instanceof ClientException) {
            return buildErrorResponse(Response.Status.SERVICE_UNAVAILABLE, e.getMessage(), Response.Status.SERVICE_UNAVAILABLE);
        } else if (e instanceof ClientWebApplicationException) {
            return buildErrorResponse(Response.Status.EXPECTATION_FAILED, e.getMessage(), Response.Status.EXPECTATION_FAILED);
        } else if (e instanceof ProcessingException) {
            return buildErrorResponse(Response.Status.REQUEST_TIMEOUT, e.getMessage(), Response.Status.REQUEST_TIMEOUT);
        } else {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, "An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Response buildErrorResponse(Response.Status errorCode, String message, Response.Status status) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(errorCode.getStatusCode())
                .errorCode(errorCode)
                .message(message)
                .build();

        return Response.status(status).entity(errorResponse).build();
    }

}
