package org.base.domain;

import jakarta.ws.rs.core.Response;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private int statusCode;
    private Response.Status errorCode;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(int statusCode, Response.Status errorCode, String message, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
