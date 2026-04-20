package com.smart.smartcampusapi.storage;

import com.smart.smartcampusapi.model.Room;
import com.smart.smartcampusapi.model.Sensor;
import com.smart.smartcampusapi.model.SensorReading;

import java.util.*;

public class DataStore {

    private static final DataStore instance = new DataStore();

    public static DataStore getInstance() {
        return instance;
    }

    private DataStore() {}

    public Map<String, Room> rooms = new HashMap<>();
    public Map<String, Sensor> sensors = new HashMap<>();
    public Map<String, List<SensorReading>> readings = new HashMap<>();
}