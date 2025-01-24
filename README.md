# Animal Kingdom Application

A Spring Boot application that provides APIs to manage a hierarchical tree structure of animals, enabling users to retrieve and manipulate the tree.


### Features

- **Get the Entire Animal Tree**  
  - Retrieve the complete tree structure of animals, including all nested children.
  - Supports hierarchical JSON output.

- **Add a New Animal**  
  - Add a new animal under a specified parent (by ID).
  - Ensures unique IDs and validates inputs.


### Technology Stack

- Java 17.
- Spring Boot 3.4.1.
- Gradle 8.12 for build and dependency management.
- JUnit 5 for testing.
- Mockito for mocking.


### API Endpoints

#### 1. Get the Entire Tree
- **Endpoint**: `GET /api/tree`
- **Description**: Returns the entire tree of animals, including all children.
- **Request Body**: None
- **Response Body**: Tree-structure with Animals (parent, label, children and ID).
- **Response Codes**: `200 OK`: Tree retrieved successfully. `404 NOT FOUND`: Tree is empty.

#### 2. Add a New Animal
- **Endpoint**: `POST /api/tree`
- **Description**: Adds a new animal under the specified parent (by ID).
- **Request Body (example)**: 
`{
  "parent": "1",
  "label": "tiger"
}`
- **Response Body (example)**: `{
  "animalId": "3",
  "label": "tiger",
  "parent": "1"
}`
- **Response Codes**: `201 CREATED`:  Animal added successfully. `400 BAD REQUEST`: Invalid input or parent not found.


### Setup

- Gradle build in the project root folder: `gradle clean build`
- Run the `AnimalKingdomApplication.java`
- Access the base URL: `http://localhost:8888`
- Access the Actuator Health URL: `GET http://localhost:8888/actuator/health`
- Get the entire Animal tree: `GET localhost:8888/api/tree`
- Add a new Animal: `POST localhost:8888/api/tree`


### Project Structure

- `controller/` for API endpoints and DTOs.
- `service/` for business logic.
- `model/` for domain model (Animal).
- `utilities/` for utilities, processing and data manipulation.
- `application.properties` for application configuration.
- `test/` for controller integration and service layers unit tests.


### Design Choices

- **Best Practices**:
  - Built using **Spring Boot**, **Java**, and **Gradle**, adhering to **MVC (Model-View-Controller)** architecture and industry best practices.

- **Model and DTOs**:
  - Introduced a dedicated `Animal` model class to represent each node in the tree.
  - Custom API DTO objects are used to decouple internal models from API payloads, enhancing maintainability.

- **Utility Classes**:
  - Utility classes were implemented to abstract:
    - Configuration logic.
    - Data processing and manipulation.
    - Error handling and logging support.

- **In-Memory Data Loader**:
  - A data loader initializes an in-memory tree data set to simulate persistence of `Animal` objects.

- **Logging**:
  - Structured logging added at both controller and service layers for better traceability.

- **Health Monitoring**:
  - Actuator's health endpoint (`/actuator/health`) is included to provide application status monitoring.

- **Configuration**:
  - Custom application configuration properties defined in `application.properties`.
  - Gradle configurations optimized for project dependencies and build management.

- **Testing**:
  - Comprehensive tests implemented:
    - **Controller integration tests** for API endpoints.
    - **Service layer unit tests** for business logic.

- **Tree Representation**:
  - The tree is represented as a `List<Map<String, Animal>>`.
  - Each `Map` contains:
    - A unique ID as the key.
    - An `Animal` object as the value.

- **ID Generation**:
  - A utility method performs a **Depth-First Search (DFS)** to find the maximum numeric ID.
  - Generates a new unique ID for any newly added `Animal` object.

- **Edge Cases**:
  - Proper handling for:
    - Adding an animal with a non-existent parent.
    - Adding an animal with a blank or malformed label.
    - Processing empty or malformed request bodies.

- **.gitignore**:
  - A `.gitignore` file is included to ensure unnecessary files (e.g., `build/` and IDE-specific files) are excluded from the repository.


### Future Improvements

- **Handle Additional Edge Cases**:
    - Prevent duplicate IDs or labels under the same parent to maintain data consistency.
    - Add safeguards for circular references in the tree structure.

- **Database Persistence**:
    - Replace the in-memory data store with a relational database such as H2 (for development) or PostgreSQL (for production).
    - Implement an efficient schema to support tree structures using parent-child relationships.

- **Swagger Integration**:
    - Use springdoc-openapi to automatically generate API documentation.
    - Provide a user-friendly UI for testing and exploring endpoints.

- **Concurrency Handling**:
    - Introduce thread safety to the tree operations to ensure consistent behavior during concurrent API requests.
    - Use synchronized blocks or thread-safe collections for shared resources.

- **Testing Enhancements**:
    - Expand test coverage to include End-to-end integration tests for the complete API.
    - Additional, edge cases like malformed input or large datasets.
    - Concurrency testing to verify thread safety.

- **Custom Error Handling and Validation**:
    - Improve error handling by introducing custom exception classes for different error scenarios.
    - A centralized exception handler using @ControllerAdvice.
    - Validate API requests with stricter rules (e.g., @NotBlank, @Size, and regex patterns).

- **Performance Optimization**:
    - Implement lazy loading to fetch only the required portion of the tree, reducing unnecessary data transfer.
    - Use caching tools like Spring Cache or Redis for frequently accessed data (e.g., root nodes or commonly requested subtrees).

- **Enhanced Security**:
    - Authentication mechanisms like Token-based authentication using JWT.
    - OAuth2 for secure user authorization.
    - Apply CORS policies to restrict API access to trusted origins.
    - Encrypt sensitive data and enforce HTTPS for communication.

- **Support for Bulk Operations**:
    - Add a dedicated bulk API (POST /api/tree/bulk) to allow the addition or update of multiple nodes in a single request.

- **Deployment and Scalability**:
    - Add containerization with Docker for consistent and portable deployments.
    - Set up CI/CD pipelines using tools like Jenkins, GitHub Actions, or GitLab CI for automated builds, testing, and deployment.

- **Monitoring and Observability**:
    - Integrate tools like Prometheus and Grafana for monitoring API performance and resource usage.
    - Add custom application metrics using Actuator and Micrometer.

#
Date: December 26, 2024

Author: Prachi Shah @ https://pcisha.my.canva.site/

P.S. The default copyright laws apply.
