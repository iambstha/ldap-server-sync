package org.base.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.base.domain.ApiResponse;
import org.base.dto.CredentialsDto;
import org.base.service.UserService;

@Path("/user")
public class UserResource {

    @Inject
    UserService userService;

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(CredentialsDto credentialsDto) {

        ApiResponse apiResponse = ApiResponse.builder()
                .data(userService.save(credentialsDto))
                .message("SUCCESS")
                .build();

        return Response.ok(apiResponse).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(CredentialsDto credentialsDto) {

        ApiResponse apiResponse = ApiResponse.builder()
                .data(userService.login(credentialsDto))
                .message("SUCCESS")
                .build();

        return Response.ok(apiResponse).build();
    }

}