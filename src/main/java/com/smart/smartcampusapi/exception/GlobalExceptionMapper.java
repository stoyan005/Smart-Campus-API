package com.smart.smartcampusapi.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        ex.printStackTrace(); // 🔥 THIS IS KEY

        return Response.status(500)
                .entity(Map.of(
                        "error", ex.getClass().getSimpleName(),
                        "message", ex.getMessage()
                ))
                .build();
    }
}