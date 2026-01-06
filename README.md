# Spring Boot Maven Monorepository with React Frontend

A comprehensive demonstration project showcasing a Maven-based monorepository with 2 Spring Boot microservices, a shared utility library, and a React TypeScript frontend - all orchestrated by Nx.

## 📁 Project Structure

```
demo-monorepository-2/
├── pom.xml                          # Parent POM
├── nx.json                          # Nx workspace configuration
├── utility-library/                 # Shared utility library
│   ├── pom.xml
│   ├── project.json                 # Nx project config
│   └── src/
│       └── main/java/
│           └── com/kreasipositif/utility/
│               ├── formatter/       # Date & Number formatters
│               ├── validator/       # String validators
│               └── generator/       # ID generators
├── service-a/                       # User Management Service (Port 8081)
│   ├── pom.xml
│   ├── project.json                 # Nx project config
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/kreasipositif/servicea/
│           │       ├── ServiceAApplication.java
│           │       ├── controller/  # REST Controllers
│           │       ├── service/     # Business Logic
│           │       ├── model/       # Domain Models
│           │       ├── dto/         # Data Transfer Objects
│           │       └── config/      # CORS Configuration
│           └── resources/
│               └── application.properties
├── service-b/                       # Order Management Service (Port 8082)
│   ├── pom.xml
│   ├── project.json                 # Nx project config
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/kreasipositif/serviceb/
│           │       ├── ServiceBApplication.java
│           │       ├── controller/  # REST Controllers
│           │       ├── service/     # Business Logic
│           │       ├── model/       # Domain Models
│           │       ├── dto/         # Data Transfer Objects
│           │       └── config/      # CORS Configuration
│           └── resources/
│               └── application.properties
└── frontend/                        # React TypeScript Frontend (Port 3000)
    ├── package.json
    ├── project.json                 # Nx project config
    ├── .env                         # Environment variables
    └── src/
        ├── components/
        │   ├── UserList.tsx         # User Management UI
        │   └── OrderList.tsx        # Order Management UI
        ├── services/
        │   ├── userService.ts       # Service A API client
        │   └── orderService.ts      # Service B API client
        ├── App.tsx
        └── index.tsx
```

## 🎯 Key Features

### Utility Library
The shared `utility-library` module provides common functionalities:

1. **DateFormatter** - Format LocalDateTime objects
2. **NumberFormatter** - Format numbers, currency, and percentages
3. **StringValidator** - Validate strings, emails, phone numbers
4. **IdGenerator** - Generate UUIDs and random alphanumeric codes

### Service A (User Management)
- **Port:** 8081
- **Purpose:** Manages user data
- **Utility Usage:**
  - `IdGenerator` for user IDs
  - `DateFormatter` for timestamp formatting
  - `StringValidator` for email and phone validation
- **API Endpoints:**
  - GET `/api/users` - List all users
  - GET `/api/users/{id}` - Get user by ID
  - POST `/api/users` - Create new user

### Service B (Order Management)
- **Port:** 8082
- **Purpose:** Manages order data
- **Utility Usage:**
  - `IdGenerator` for order IDs and order numbers
  - `DateFormatter` for timestamp formatting
  - `NumberFormatter` for currency formatting
  - `StringValidator` for input validation
- **API Endpoints:**
  - GET `/api/orders` - List all orders
  - GET `/api/orders/{id}` - Get order by ID
  - POST `/api/orders` - Create new order

### Frontend (React Dashboard)
- **Port:** 3000
- **Technology:** React 18 + TypeScript
- **Features:**
  - � User Management UI (connects to Service A)
  - 📦 Order Management UI (connects to Service B)
  - ✨ Real-time updates
  - 📝 Form validation
  - 🎨 Responsive design
- **Integration:**
  - Consumes Service A REST API
  - Consumes Service B REST API
  - CORS enabled for cross-origin requests

## �🚀 Getting Started

### Prerequisites
- **Java 17+** - For Spring Boot services
- **Maven 3.6+** - For building Java projects
- **Node.js 14+** - For React frontend
- **npm or yarn** - For frontend dependencies

### Quick Start (Full Stack)

```bash
# 1. Build all Java modules
mvn clean install

# 2. Start Service A (Terminal 1)
cd service-a && mvn spring-boot:run

# 3. Start Service B (Terminal 2)
cd service-b && mvn spring-boot:run

# 4. Setup and start Frontend (Terminal 3)
./setup-frontend.sh
cd frontend && npm start
```

Access the application:
- **Frontend Dashboard:** http://localhost:3000
- **Service A API:** http://localhost:8081
- **Service B API:** http://localhost:8082

### Building with Maven

Build all modules from the root directory:

```bash
mvn clean install
```

This will:
1. Build the `utility-library` module
2. Build `service-a` and `service-b` (which depend on `utility-library`)

### Running the Services

**Option 1: Run from individual module directories**

Terminal 1 - Service A:
```bash
cd service-a
mvn spring-boot:run
```

Terminal 2 - Service B:
```bash
cd service-b
mvn spring-boot:run
```

**Option 2: Run the JAR files**

After building, run:
```bash
java -jar service-a/target/service-a-1.0.0-SNAPSHOT.jar
java -jar service-b/target/service-b-1.0.0-SNAPSHOT.jar
```

## 📝 API Endpoints

### Service A - User Management (Port 8081)

**Create User**
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890"
  }'
```

**Get All Users**
```bash
curl http://localhost:8081/api/users
```

**Get User by ID**
```bash
curl http://localhost:8081/api/users/{id}
```

### Service B - Order Management (Port 8082)

**Create Order**
```bash
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-123",
    "productName": "Laptop",
    "quantity": 2,
    "unitPrice": 999.99
  }'
```

**Get All Orders**
```bash
curl http://localhost:8082/api/orders
```

**Get Order by ID**
```bash
curl http://localhost:8082/api/orders/{id}
```

## 🧪 Testing the Shared Utility Library

Both services use the same utility library instances:

1. **Service A** uses:
   - `IdGenerator.generateUUID()` for user IDs
   - `DateFormatter.formatDateTime()` for timestamps
   - `StringValidator.isValidEmail()` for email validation
   - `StringValidator.isValidPhone()` for phone validation

2. **Service B** uses:
   - `IdGenerator.generateUUID()` for order IDs
   - `IdGenerator.generateAlphanumeric(8)` for order numbers
   - `DateFormatter.formatDateTime()` for timestamps
   - `NumberFormatter.formatCurrency()` for prices
   - `NumberFormatter.formatNumber()` for quantities

## 🔧 Benefits of Monorepository Structure

1. **Code Reusability** - Share common utilities across multiple services
2. **Consistent Versioning** - All modules use the same version
3. **Simplified Dependency Management** - Parent POM manages all dependencies
4. **Atomic Changes** - Update shared library and all consumers in one commit
5. **Easier Development** - No need to publish artifacts to remote repositories during development

## 📊 Dependency Graph

```
service-a  ──→  utility-library
service-b  ──→  utility-library
```

Both services depend on the utility library, which is built first during `mvn install`.

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.2.1**
- **Maven** - Build tool and dependency management
- **Lombok** - Reduce boilerplate code
- **Spring Boot Actuator** - Production-ready features

## 📖 Blog Post Topics

This repository can be used to demonstrate:

1. **Setting up a Maven monorepository**
2. **Creating shared libraries in Spring Boot**
3. **Managing dependencies in multi-module projects**
4. **Spring Boot auto-configuration for shared libraries**
5. **Best practices for microservices code sharing**
6. **Building and deploying multi-module Maven projects**

## 🤝 Contributing

This is a demonstration project for educational purposes.

## 📄 License

This project is created for demonstration purposes.

---

Created with ❤️ by Kreasipositif
