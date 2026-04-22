# Student Management and Fee Collection System

A comprehensive microservices-based application built with Spring Boot 3 for managing student information and fee collection processes.

## 🏗️ Architecture Overview

This system consists of two main microservices:

### 1. Student Management Service
- **Purpose**: Manage student information including registration, updates, and retrieval
- **Features**: CRUD operations, search functionality, grade-based filtering
- **Base URL**: `http://localhost:8080/api/v1/students`

### 2. Fee Collection Service
- **Purpose**: Handle fee collection and receipt generation
- **Features**: Fee collection, receipt management, financial reporting
- **Base URL**: `http://localhost:8080/api/v1/fee`

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- Git

### Installation and Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd apiAssesment
   ```

2. **Build the application**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Application runs on port `8080`
   - H2 Database Console: `http://localhost:8080/h2-console`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

### Database Configuration

The application uses H2 in-memory database with the following settings:
- **URL**: `jdbc:h2:mem:studentdb`
- **Username**: `sa`
- **Password**: `password`
- **Console**: Available at `/h2-console`

## 📚 API Documentation

### Swagger/OpenAPI Documentation
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

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
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=StudentServiceTest

# Run tests with coverage
mvn clean test jacoco:report
```

### Sample Data
The application automatically loads sample data on startup, including:
- 8 sample students
- 8 sample fee receipts

## 📦 Postman Collections

Two Postman collections are provided for easy API testing:

1. **Student Management API Collection**
   - File: `Student-Management-API.postman_collection.json`
   - Contains all student-related API endpoints

2. **Fee Collection API Collection**
   - File: `Fee-Collection-API.postman_collection.json`
   - Contains all fee collection API endpoints

### Importing Postman Collections
1. Open Postman
2. Click on "Import" button
3. Select the JSON files from the project root
4. The collections will be imported with all endpoints and sample requests

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

### ✅ Student Management
- [x] Student registration with validation
- [x] Student information updates
- [x] Student search (name, ID, mobile)
- [x] Grade-based filtering
- [x] Soft delete functionality
- [x] Active/inactive status management

### ✅ Fee Collection
- [x] Fee collection with receipt generation
- [x] Multiple payment methods support
- [x] Academic year tracking
- [x] Comprehensive receipt management
- [x] Financial reporting and analytics
- [x] Date range filtering
- [x] Fee type categorization

### ✅ Technical Features
- [x] Spring Boot 3.2.x
- [x] Spring Data JPA
- [x] H2 in-memory database
- [x] OpenAPI 3.0 documentation
- [x] Comprehensive unit tests
- [x] Input validation
- [x] Error handling
- [x] Logging

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

### Docker Support (Optional)
```bash
# Build Docker image
docker build -t student-fee-system .

# Run container
docker run -p 8080:8080 student-fee-system
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
