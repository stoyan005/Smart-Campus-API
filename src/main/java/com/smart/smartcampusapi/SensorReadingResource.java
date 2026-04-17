package com.smart.smartcampusapi;

import com.smart.smartcampusapi.model.SensorReading;
import com.smart.smartcampusapi.model.Sensor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;

@Path("/sensors/{id}/readings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    @PathParam("id")
    private String sensorId;

    private static Map<String, List<SensorReading>> readings = new HashMap<>();

    @GET
    public List<SensorReading> getReadings() {
        return readings.getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    public Response addReading(SensorReading reading) {

        Sensor sensor = SensorResource.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found"))
                    .build();
        }

        reading.setId(UUID.randomUUID().toString());

        readings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}