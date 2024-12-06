package org.base.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.core.Response;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class ApiResponse {

    private String status;
    private int statusCode;
    private String message;
    private Object data;
    private Object metadata;

    public ApiResponse() {
        this.status = Response.Status.OK.getReasonPhrase();
        this.statusCode = Response.Status.OK.getStatusCode();
        this.message = "";
        this.data = null;
        this.metadata = null;
    }

    public static ApiResponseBuilder builder() {
        ApiResponseBuilder builder = new ApiResponseBuilder();
        builder.status(Response.Status.OK.getReasonPhrase());
        builder.statusCode(Response.Status.OK.getStatusCode());
        return builder;
    }

}
