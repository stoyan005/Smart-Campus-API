package com.smart.smartcampusapi.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;
import com.smart.smartcampusapi.exception.RoomNotEmptyException;

@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        return Response.status(409)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}