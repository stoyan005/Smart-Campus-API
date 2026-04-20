# Smart Campus REST API (JAX-RS)

## Overview

This project implements a RESTful API for managing a **Smart Campus system**, including Rooms, Sensors, and Sensor Readings.

The API follows REST principles:

* Resource-based URLs
* Proper HTTP methods
* Meaningful status codes
* JSON communication

### Core Resources

* **Rooms** → `/api/v1/rooms`
* **Sensors** → `/api/v1/sensors`
* **Sensor Readings** → `/api/v1/sensors/{id}/readings`

---

## API Design Highlights

* Built using **JAX-RS (Jersey)**
* Versioned API: `/api/v1`
* In-memory storage using `HashMap` / `ArrayList`
* Nested resources for readings
* Filtering using query parameters
* Custom exception handling (409, 422, 403, 500)
* Logging via JAX-RS filters

---

## ⚙️ How to Build & Run

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

## Sample cURL Commands

### 1. Get API Discovery

```bash
curl -X GET http://localhost:8080/api/v1
```

---

### 2. Create a Room

```bash
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{
  "id": "LIB-301",
  "name": "Library Study Room",
  "capacity": 50
}'
```

---

### 3. Get All Rooms

```bash
curl -X GET http://localhost:8080/api/v1/rooms
```

---

### 4. Create a Sensor

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

---

### 5. Filter Sensors by Type

```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
```

---

### 6. Add Sensor Reading

```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{
  "id": "r1",
  "timestamp": 1710000000000,
  "value": 23.1
}'
```

---

### 7. Delete Room (Fails if sensors exist)

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

* No database used (as required)
* Uses in-memory data structures
* Designed for scalability and maintainability

---

## Demo

Include your video demo link here:

```
[Paste your Blackboard or YouTube link]
```

---
