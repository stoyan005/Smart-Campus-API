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
### Base Path: ```bash /api/v1```

* Discovery – GET / – ```Returns API metadata and resource links```
* Rooms – ```/rooms```
* GET /rooms – ```List all rooms```
* POST /rooms – ```Create a new room```
* GET /rooms/{roomId} – ```Get room by ID```
* DELETE /rooms/{roomId} – ```Delete a room (with safety check)```
---
Sensors – /sensors
* GET /sensors – ```List all sensors (includes ?type=CO2)```
* POST /sensors – ```Create a new sensor```
* GET /sensors/{sensorId} – ```Get sensor by ID```
---
Sensor Readings (nested sub-resource)
* GET /sensors/{sensorId}/readings – ```Get reading history```
* POST /sensors/{sensorId}/readings – ```Add a new reading```

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
# Conceptual Report
---


### Part 1: Service Architecture & Setup 
1.	Project & Application Configuration:
In JAX-RS, a resource class follows a per-request lifecycle, which means a new instance of the resource class is created for each incoming HTTP request. This design ensures that resource classes remain stateless and align with REST principles to prevent unintended data sharing between different client requests.
However, in this coursework the application data such as Rooms and Sensors, is stored in-memory collections (HashMap or ArrayList) that must be persistent across multiple requests. To achieve this, these collections are defined in a shared context, such as static variables or singleton service classes so that all resource instances can access the same data at any given time.
If the collections were not stored in a shared context, they would be created as instance variables within the resource class, and since a new resource instance is created for each request, the data would not persist, and any information stored during one request would be lost once the request is completed. As a result, the application would behave as if it “forgets” all previously stored data between requests.
Because these shared collections are accessed by multiple requests constantly, there is a rice of race conditions, data loss or even inconsistent updates, this is preventable by using thread-safe techniques such as concurrent collections (ConcurrentHashMap) and applying controlled synchronisation mechanics.

2.	Why Hypermedia (HATEOAS)?:
Hypermedia as the Engine of Application State (HATEOAS) is a key REST constraint where each server response includes links that guide the client to the related resources and potentially their next actions. Rather than relying on hardcoded endpoint URLs, the client discovers how to interact with the API by following the provided links.
This approach not only improves decoupling because the client is no longer fixated to URI structures but also allows for more flexible API evolution, since changes to endpoints can be introduced without breaking existing clients. In addition to this, it enables the client to discover available actions overtime based on the responses, allowing the system to become self-descriptive and reduces the reliance on external documentation and allows clients to navigate through the system based on information returned by the server itself.
Overall, HATEOAS leads to flexibility and maintainable systems, since changes can be introduced on the server side with little to no impact on clients while continuing to improve the overall usability of the API.


### Part 2: Room Management
1.	Room Resource Implementation
Returning only small room IDs reduces the network bandwidth usage because IDs are small and lightweight compared to full room objects. This makes the responses faster and more efficient especially when dealing with larger datasets or limited network conditions.
However, returning only IDs increases client-side processing, the client then must make additional requests to fetch full room details or maintain the logic to resolve those IDs into usable data again, which can add complexity and latency especially if done manually. The downside is that full objects consume more bandwidth, which slows down responses and increases data transfer costs, especially if many rooms are being returned or if the objects contain a lot of data.
Overall, returning IDs is more efficient and effective for performance and bandwidth reasons, while returning full objects is more convenient for the client, it is also very costly, and the best choice depends on the use case and system constraints.

2.	Room Deletion & Safety Logic
The DELETE operation is idempotent in my implementation, as Idempotency means that making the same request multiple times results in the same overall state on the server as making it once.
When a client sends a DELETE request for a room the first time, the server locates the room by its ID and removes it from the database/in-memory. The room no longer exists after this operation, and the system state is updated accordingly.
If the client sends the exact same DELETE request again, the server will attempt to find the room using the same ID, but since the room has already been deleted previously, it will not be found. In an idempotent design the server will continue to ensure that the final state remains unchanged (the room stays deleted), and the server may respond with a “404 NOT FOUND” or “204 NO CONTENT” errors, but no additional changes will occur to the system moving forward.
Overall, regardless of how many times the same DELETE request is repeated the outcome will remain the same: the room does not exist. This consistent ending is what makes the DELETE operation idempotent.


### Part 3: Sensor Operations & Linking
1.	Sensor Resource & Integrity
Using @Consumes(MediaType.APPLICATION_JSON tells JAX-RS that the endpoint only accepts requests with a Content-Type of application/json. If a client sends data in a different format, such as text/plain or application/xml, JAX-RS will not consider the request compatible with that method.
As a result, the framework will reject the request before it even reaches the resource method and typically returns a 415 Unsupported Media Type response. This ensures that the server only processes data it knows how to correctly parse, which helps prevent errors or undefined behavior caused by unexpected input formats.

2.	Filtered Retrieval & Search
Using @QueryParam for filtering (in /api/v1/sensors?type=Pressure) is generally preferred because it clearly expresses that the client is querying a collection with optional criteria rather than accessing different resources. Query parameters are designed for filtering, sorting and searching so this approach aligns better with RESTful principles and keeps the flexibility on endpoint as more filters may be added.
On the other hand, embedding the filter in the path (in /api/v1/sensors?type=Pressure) makes the URL more tedious and harder to extend. Every new filter that is added requires a new path structure, which leads to less scalable and more complex API design and longer URL paths. Query parameters allow multiple filters to be combined easily to keep the URL structure maintained and clean.


### Part 4: Deep Nesting with Sub-Resources
1.	The Sub-Resource Locater Pattern:
The Sub-Resource Locater pattern serves as a sophisticated architectural strategy to uphold the Single Responsibility Principle, effectively preventing the emergence of “God Classes” (anti-pattern that consists of having a class that does too much) inside the RESTful API. In standard monolithic controller, the requirement to define every nested path, such as sensors/{id}/readings/{rid} – forces a single class to manage a large array of dependencies, protocol, logic for multiple unrelated entities. By allocating this logic to dedicated sub-resource classes, it allows us to take off the load and relocate it to dedicated primary controllers that can be refined into a focused entry point that is responsible for initial resource identifications and routing instead of handling every sort of entity which leads to a “God Class” and ensures that the codebase remains logically structured and navigable as it’s easier to read and understand, since the API mirrors hierarchical object graph rather than a flat one.
Furthermore, this distribution significantly improves long-term maintainability and usability. Instead of every method having to manually extract IDs from a URL or re-fetch data from a database, the parent controller can simply pass a ready-to-use object directly into the sub-resources constructor to remove a lot of the repetitive “boilerplate” code and speed up the development process. From a testing standpoint, you can test a sub-resource (like Sensors or Readings) on its own without needing to set up the entire “Sensor” environment. This decoupling allows the API to grow deeper and more complex without the code becoming a tangled mess that is impossible to debug.


### Part 5: Advanced Error Handling, Exception Mapping & Logging 
1.	Dependency Validation (422 Unprocessable Entity)
A 404 Not Found status is designed to indicate that the URI itself does not point to a recognized resource. If a client sends a request to POST /api/sensors/999/readings, and the sensor 999 does not exist, a 404 is properly displayed because the path is invalid. However, if the client sends a valid request to POST /api/readings with a JSON body that contains {“sensor_id”: 999}, then URI is perfectly correct and a 404 will not be displayed. The server successfully reached the controller and parsed the JSON, but it cannot “process” the request because the data inside is logically inconsistent. Using a 422 ultimately tells the client: “I understand your format and reached the right place, but the data you provided is semantically broken”.
Using a 422 is more helpful for debugging the client-side logic. If a client receives a 404, it might assume that it hit a dead link or invalid endpoint. Contrary to this, 422 signals a validation error. This allows the client to distinguish between a structural failure (URL is entered wrong) and a data failure (the specific ID provided does not exist in the database) and can determine either one simply by looking at both HTTP protocols. By returning a 422, the server can include a response body explaining exactly which field failed validation and makes the API easily predictable.

2.	The Global Safety Net (500)
From a cybersecurity standpoint, exposing internal Java stack traces to external API consumers creates information disclosure risks. Stack traces are meant to be used for debugging and not public consumption and revealing them can give attackers insight into the internal workings of the applications itself.
A stack trace can expose certain details about the application’s structure, security, package hierarchies, method calls and other important datasets which help the attackers understand how the system works and is organized and gives them an open path to identify potential weaknesses and points to target, such as vulnerabilities in poorly handled outputs or components.
It may also reveal the technical aspects and frameworks used like libraries, versions, servers and with this information an attack is able to look up vulnerabilities associated with these technologies and attempt to target them for exploitation.
Overall, exposing stack traces to the public eye increases the risk of surface attacks by providing valuable sensitivity that can be easily compromised and speed up the attacking process for more precise and effective attacks to occur.

3.	API Request & Response Logging Filters
Using JAX-RS filters for logging allows for centralized cross-cutting concerns in single places, avoiding the need to add Logger.info() statements in every resource method. This helps reduce code duplication and ensures consistent logging across all API endpoints. If there needs to be any format or extra logging requirement changes done, it can be updated in one place rather than throughout the entire codebase. It also provides a more comprehensive way to capture requests and response data that enhances monitoring and debugging compared to manually inserting log statements.

---

<h1 align="center">END OF REPORT</h1>

---
