# Student Management and Fee Collection System - Microservices Architecture

A comprehensive microservices-based application built with Spring Boot 3 for managing student information and fee collection processes.

## 🏗️ Architecture Overview

This system has been refactored from a monolithic application to a proper microservices architecture with the following components:

### 1. API Gateway (Port 8080)
- **Purpose**: Central entry point for all API requests
- **Features**: Request routing, load balancing, circuit breaker, response aggregation
- **Base URL**: `http://localhost:8080`

### 2. Student Management Service (Port 8081)
- **Purpose**: Manage student information including registration, updates, and retrieval
- **Features**: CRUD operations, search functionality, grade-based filtering
- **Base URL**: `http://localhost:8081/api/v1/students`

### 3. Fee Collection Service (Port 8082)
- **Purpose**: Handle fee collection and receipt generation
- **Features**: Fee collection, receipt management, financial reporting
- **Base URL**: `http://localhost:8082/api/v1/fee`

### 4. Inter-Service Communication
- **Technology**: Spring Cloud OpenFeign for service-to-service communication
- **Resilience**: Circuit Breaker pattern with Resilience4j
- **Fallback**: Graceful degradation when services are unavailable

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- Docker and Docker Compose (for containerized deployment)
- Git

### Option 1: Running Individual Services

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd apiAssesment
   ```

2. **Build all services**
   ```bash
   # Build Student Service
   cd student-service
   mvn clean install
   
   # Build Fee Service
   cd ../fee-service
   mvn clean install
   
   # Build API Gateway
   cd ../api-gateway
   mvn clean install
   ```

3. **Run services in order**
   ```bash
   # Terminal 1 - Student Service
   cd student-service
   mvn spring-boot:run
   
   # Terminal 2 - Fee Service
   cd fee-service
   mvn spring-boot:run
   
   # Terminal 3 - API Gateway
   cd api-gateway
   mvn spring-boot:run
   ```

### Option 2: Docker Compose (Recommended)

1. **Build and run all services**
   ```bash
   docker-compose up --build
   ```

2. **Access the application**
   - API Gateway: `http://localhost:8080`
   - Student Service: `http://localhost:8081`
   - Fee Service: `http://localhost:8082`
   - Swagger UI (Gateway): `http://localhost:8080/swagger-ui.html`
   - Health Checks: `http://localhost:8080/actuator/health`

### Database Configuration

Each microservice uses its own H2 in-memory database:

**Student Service Database:**
- **URL**: `jdbc:h2:mem:studentdb`
- **Console**: `http://localhost:8081/h2-console`

**Fee Service Database:**
- **URL**: `jdbc:h2:mem:feedb`
- **Console**: `http://localhost:8082/h2-console`

**Production Database (Docker):**
- **Student DB**: MySQL on port 3306
- **Fee DB**: MySQL on port 3307

## 📚 API Documentation

### Swagger/OpenAPI Documentation
- **API Gateway**: `http://localhost:8080/swagger-ui.html`
- **Student Service**: `http://localhost:8081/swagger-ui.html`
- **Fee Service**: `http://localhost:8082/swagger-ui.html`
- **Gateway OpenAPI JSON**: `http://localhost:8080/api-docs`

### Student Management APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/students` | Create a new student |
| GET | `/api/v1/students` | Get all students |
| GET | `/api/v1/students/{id}` | Get student by ID |
| GET | `/api/v1/students/by-student-id/{studentId}` | Get student by student ID |
| PUT | `/api/v1/students/{id}` | Update student information |
| DELETE | `/api/v1/students/{id}` | Delete student |
| PATCH | `/api/v1/students/{id}/deactivate` | Deactivate student (soft delete) |
| GET | `/api/v1/students/search?search={term}` | Search students |
| GET | `/api/v1/students/by-grade/{grade}` | Get students by grade |
| GET | `/api/v1/students/active` | Get active students |

### Fee Collection APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/fee/collect` | Collect fee and generate receipt |
| GET | `/api/v1/fee/receipts` | Get all receipts |
| GET | `/api/v1/fee/receipts/{id}` | Get receipt by ID |
| GET | `/api/v1/fee/receipts/by-number/{receiptNumber}` | Get receipt by receipt number |
| GET | `/api/v1/fee/receipts/student/{studentId}` | Get receipts by student ID |
| GET | `/api/v1/fee/receipts/academic-year/{academicYear}` | Get receipts by academic year |
| GET | `/api/v1/fee/total/student/{studentId}/academic-year/{academicYear}` | Get total fees paid by student |
| GET | `/api/v1/fee/total/academic-year/{academicYear}` | Get total fees collected |
| GET | `/api/v1/fee/receipts/search?search={term}` | Search receipts |
| GET | `/api/v1/fee/receipts/date-range?startDate={start}&endDate={end}` | Get receipts by date range |
| GET | `/api/v1/fee/receipts/fee-type/{feeType}` | Get receipts by fee type |
| GET | `/api/v1/fee/receipts/payment-method/{paymentMethod}` | Get receipts by payment method |
| GET | `/api/v1/fee/receipt-number/generate` | Generate unique receipt number |

## 🧪 Testing

### Running Tests
```bash
# Run all tests for Student Service
cd student-service
mvn test

# Run all tests for Fee Service
cd fee-service
mvn test

# Run specific test class
mvn test -Dtest=StudentServiceTest

# Run tests with coverage
mvn clean test jacoco:report
```

### Test Coverage
- **Student Service**: >90% code coverage
- **Fee Service**: >90% code coverage
- **Controller Tests**: Full endpoint testing
- **Service Tests**: Business logic validation
- **Integration Tests**: Cross-service communication

### Health Checks and Monitoring
Each service includes comprehensive health checks:
- **Student Service**: `http://localhost:8081/actuator/health`
- **Fee Service**: `http://localhost:8082/actuator/health`
- **API Gateway**: `http://localhost:8080/actuator/health`
- **Circuit Breaker Status**: `http://localhost:8080/actuator/circuitbreakers`

## 📦 Postman Collections

A comprehensive Postman collection is provided for complete API testing:

1. **Microservices API Collection**
   - File: `Microservices-API-Collection.postman_collection.json`
   - Contains all endpoints for all services
   - Includes health checks and error handling tests
   - Environment variables for easy configuration
   - Automated test scripts for response validation

### Importing Postman Collections
1. Open Postman
2. Click on "Import" button
3. Select the `Microservices-API-Collection.postman_collection.json` file
4. The collection will be imported with:
   - All API endpoints organized by service
   - Sample request bodies
   - Environment variables (`{{base_url}}`)
   - Automated test scripts
   - Error handling test cases

## 🏛️ Domain Models

### Student Entity
```java
{
    "id": Long,
    "studentName": String,
    "studentId": String,
    "grade": String,
    "mobileNumber": String,
    "schoolName": String,
    "active": Boolean
}
```

### Receipt Entity
```java
{
    "id": Long,
    "receiptNumber": String,
    "studentId": Long,
    "studentName": String,
    "feeType": String,
    "amount": BigDecimal,
    "paymentDate": LocalDateTime,
    "paymentMethod": String,
    "transactionId": String,
    "academicYear": String,
    "month": String,
    "quarter": String,
    "remarks": String,
    "createdAt": LocalDateTime,
    "createdBy": String
}
```

## 🔧 Configuration

### Application Properties
Key configuration options in `application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:h2:mem:studentdb
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# OpenAPI Configuration
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/api-docs
```

## 📊 Features Implemented

### ✅ Microservices Architecture
- [x] **Service Separation**: Student and Fee services as independent microservices
- [x] **API Gateway**: Central routing and load balancing
- [x] **Inter-Service Communication**: OpenFeign for service-to-service calls
- [x] **Circuit Breaker Pattern**: Resilience4j for fault tolerance
- [x] **Service Discovery**: Configured service endpoints
- [x] **Database Per Service**: Separate databases for each microservice

### ✅ Student Management Service
- [x] Student registration with validation
- [x] Student information updates
- [x] Student search (name, ID, mobile)
- [x] Grade-based filtering
- [x] Soft delete functionality
- [x] Active/inactive status management
- [x] Comprehensive error handling
- [x] Audit fields (created/updated timestamps)

### ✅ Fee Collection Service
- [x] Fee collection with receipt generation
- [x] Multiple payment methods support
- [x] Academic year tracking
- [x] Comprehensive receipt management
- [x] Financial reporting and analytics
- [x] Date range filtering
- [x] Fee type categorization
- [x] Student validation via inter-service communication

### ✅ Resiliency and Error Handling
- [x] **Circuit Breaker**: Automatic fallback when services are unavailable
- [x] **Global Exception Handling**: Centralized error responses
- [x] **Input Validation**: Bean validation with proper error messages
- [x] **Retry Mechanisms**: Configurable retry policies
- [x] **Fallback Responses**: Graceful degradation

### ✅ Technical Features
- [x] Spring Boot 3.2.x
- [x] Spring Cloud Gateway
- [x] Spring Cloud OpenFeign
- [x] Spring Data JPA
- [x] Resilience4j Circuit Breaker
- [x] H2 in-memory database (development)
- [x] MySQL database (production/Docker)
- [x] OpenAPI 3.0 documentation
- [x] Comprehensive unit tests (>90% coverage)
- [x] Docker containerization
- [x] Docker Compose orchestration
- [x] Health checks and monitoring
- [x] Structured logging

## 🧪 Quality Metrics

### Code Coverage
- Student Service: >90%
- Fee Collection Service: >90%
- Controllers: >85%

### REST API Best Practices
- ✅ Proper HTTP status codes
- ✅ Resource naming conventions
- ✅ HTTP method usage
- ✅ Request/response validation
- ✅ Error handling

## 🚀 Deployment

### Docker Deployment (Recommended)
```bash
# Build and run all services with Docker Compose
docker-compose up --build

# Run in background
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f

# Scale specific service
docker-compose up --scale student-service=2
```

### Individual Service Deployment
```bash
# Build Student Service
cd student-service
docker build -t student-service .
docker run -p 8081:8081 student-service

# Build Fee Service
cd fee-service
docker build -t fee-service .
docker run -p 8082:8082 fee-service

# Build API Gateway
cd api-gateway
docker build -t api-gateway .
docker run -p 8080:8080 api-gateway
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Run the test suite
6. Submit a pull request

## 📝 License

This project is licensed under the MIT License.

## 📞 Support

For any queries or issues, please refer to:
- API Documentation: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`
- Application Logs: Console output

## 🔍 Monitoring and Logging

The application includes comprehensive logging:
- DEBUG level for application components
- SQL query logging enabled
- Hibernate parameter binding logging

Access logs and application logs in the console when running the application.
