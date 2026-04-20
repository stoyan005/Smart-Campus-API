package com.smart.smartcampusapi;

import com.smart.smartcampusapi.RoomResource;
import com.smart.smartcampusapi.model.Sensor;
import com.smart.smartcampusapi.model.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;
import com.smart.smartcampusapi.exception.LinkedResourceNotFoundException;
import com.smart.smartcampusapi.SensorReadingResource;
import com.smart.smartcampusapi.SensorReadingResource;
import com.smart.smartcampusapi.model.Room;
import com.smart.smartcampusapi.model.Sensor;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    public static Map<String, Sensor> sensors = new HashMap<>();

    @GET
    public Collection<Sensor> getSensors(@QueryParam("type") String type) {

        if (type == null || type.isEmpty()) {
            return sensors.values();
        }

        List<Sensor> filtered = new ArrayList<>();

        for (Sensor sensor : sensors.values()) {
            if (sensor.getType().equalsIgnoreCase(type)) {
                filtered.add(sensor);
            }
        }

        return filtered;
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {

        Room room = RoomResource.rooms.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException("Room does not exist");
        }

        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Sensor ID is required"))
                    .build();
        }

        sensors.put(sensor.getId(), sensor);

        room.getSensorIds().add(sensor.getId());

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(sensor.getId());

        return Response.created(builder.build())
                .entity(sensor)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getSensor(@PathParam("id") String id) {

        Sensor sensor = sensors.get(id);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(sensor).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSensor(@PathParam("id") String id) {

        Sensor sensor = sensors.get(id);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Room room = RoomResource.rooms.get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(id);
        }

        sensors.remove(id);

        return Response.noContent().build();
    }

    @Path("/{id}/readings")
    public SensorReadingResource getReadingResource(@PathParam("id") String id) {
        return new SensorReadingResource();
}
}