package com.smart.smartcampusapi.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class LinkedResourceNotFoundMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    @Override
        public Response toResponse(LinkedResourceNotFoundException ex) {
 
        String json = "{\"error\": \"Linked resource not found\", \"message\": \"" + ex.getMessage() + "\"}";
    
        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(json)
                .build();
}
}