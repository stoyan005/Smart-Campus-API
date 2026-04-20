# Smart Campus REST API (JAX-RS)

## Overview

This project implements a RESTful API for managing a Smart Campus system, including Rooms, Sensors, and Sensor Readings. The API provides a structured way for clients to create, retrieve, update, and manage campus resources, enabling efficient monitoring of room usage and environmental data through connected sensors. It follows RESTful principles, using clear resource-based endpoints, appropriate HTTP methods, and JSON for data exchange, ensuring scalability, maintainability, and ease of integration with other systems.

---

**The API follows REST principles:**

* Resource-based URLs
* Proper HTTP methods
* Meaningful status codes
* JSON communication

### Core Resources

* **Rooms** → `/api/v1/rooms`
* **Sensors** → `/api/v1/sensors`
* **Sensor Readings** → `/api/v1/sensors/{id}/readings`

---

## API Features & Highlights

- Built using JAX-RS (Jersey)
- Versioned API: `/api/v1`
- In-memory storage (HashMap / ArrayList)
- Nested resources (sensor readings)
- Query parameter filtering
- Custom exception handling:
  - 409 Conflict
  - 422 Unprocessable Entity
  - 403 Forbidden
  - 500 Internal Server Error
- Logging using JAX-RS filters

---

## How to Run (NetBeans)

1. Open NetBeans
2. Click **File → Open Project**
3. Select the project folder: `smart-campus-api`
4. Wait for Maven dependencies to load
5. Right-click project → **Clean and Build**
6. Right-click project → **Run**
7. Open:

---

## How to Run (IntelliJ IDEA)

1. Open IntelliJ IDEA
2. Click **Open**
3. Select project folder: `smart-campus-api`
4. Wait for Maven import to finish
5. Run:
```bash
mvn clean install
```
```bash
mvn jetty:run
```
```bash
http://localhost:8080/api/v1
```

---


## How to Build & Run

### 1. Clone Repository

```bash
git clone https://github.com/your-username/smart-campus-api.git
cd smart-campus-api
```

### 2. Build Project

```bash
mvn clean install
```

### 3. Run Server

```bash
mvn exec:java
```

OR (if using embedded server):

```bash
mvn jetty:run
```

### 4. Access API

```
http://localhost:8080/api/v1
```

---

## cURL Commands

### 1. Get API Discovery

```bash
curl -X GET http://localhost:8080/api/v1
```

---

### 2. Create a Room (POST)

```bash
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{
  "id": "LIB-301",
  "name": "Library Study Room",
  "capacity": 50
}'
```

### 2.1 Get All Rooms (GET)
```bash
curl -X GET http://localhost:8080/api/v1/rooms
```

### 2.2 Get Room by ID (GET)
```bash
curl -X GET http://localhost:8080/api/v1/rooms/LIB-301
```

### 2.3 Delete Room (DELETE)
```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```
---

### 3. Create a Sensor

```bash
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{
  "id": "TEMP-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 22.5,
  "roomId": "LIB-301"
}'
```

### 3.1 Get All Sensors (GET)
```bash
curl -X GET http://localhost:8080/api/v1/sensors
```

### 3.2 Get Sensor by ID (GET)
```bash
curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001
```

### 3.3 Filter Sensors by Type
```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
```

### 3.4 Delete Sensor (DELETE)
```bash
curl -X DELETE http://localhost:8080/api/v1/sensors/TEMP-001
```
---

### 4. Add Sensor Reading

```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{
  "id": "r1",
  "timestamp": 1710000000000,
  "value": 23.1
}'
```

### 4.1 Get All Readings (GET)
```bash
curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001/readings
```

### 4.2 Get Reading by ID (GET)
```bash
curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001/readings/r1
```

---

### 5. Delete Room (Fails if sensors exist)

```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```

---

## Error Handling

| Scenario               | Status Code               |
| ---------------------- | ------------------------- |
| Room not empty         | 409 Conflict              |
| Invalid room reference | 422 Unprocessable Entity  |
| Sensor unavailable     | 403 Forbidden             |
| Unexpected error       | 500 Internal Server Error |

---

## Logging

* Logs all incoming requests (method + URI)
* Logs all outgoing responses (status code)
* Implemented using JAX-RS filters

---

## Notes

* No database used (in-memory storage only as required)
* Uses in-memory data structures
* Rooms must exist before sensors are created
* Sensors must exist before readings are added
* Rooms cannot be deleted if sensors exist
* Designed for scalability and maintainability

---

## Demo

Link to Video:

```
[video will go here when finished]
```

---
