package com.smart.smartcampusapi.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;
import com.smart.smartcampusapi.exception.SensorUnavailableException;

@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {
        return Response.status(403)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}