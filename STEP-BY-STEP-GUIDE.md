# Step-by-Step Guide: Creating a Spring Boot Maven Monorepository

This guide walks you through creating a Maven-based monorepository with 2 Spring Boot applications sharing a common utility library.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Step 1: Create Parent POM](#step-1-create-parent-pom)
3. [Step 2: Create Shared Utility Library](#step-2-create-shared-utility-library)
4. [Step 3: Create Service A](#step-3-create-service-a)
5. [Step 4: Create Service B](#step-4-create-service-b)
6. [Step 5: Build and Test](#step-5-build-and-test)
7. [Step 6: Run the Applications](#step-6-run-the-applications)

---

## Prerequisites

Before starting, ensure you have:
- Java 17 or higher installed
- Maven 3.6 or higher installed
- Your favorite IDE (IntelliJ IDEA, Eclipse, or VS Code)

Verify installations:
```bash
java -version
mvn -version
```

---

## Step 1: Create Parent POM

### Purpose
The parent POM acts as the root of your monorepository, managing:
- Common dependencies and versions
- Build plugins
- Module definitions

### Implementation

Create `pom.xml` in the root directory:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.kreasipositif</groupId>
    <artifactId>demo-monorepository</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>utility-library</module>
        <module>service-a</module>
        <module>service-b</module>
    </modules>

    <properties>
        <java.version>17</java.version>
        <spring-boot.version>3.2.1</spring-boot.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

### Key Points
- `<packaging>pom</packaging>` - Indicates this is a parent/aggregator project
- `<modules>` - Lists all sub-modules
- `<dependencyManagement>` - Defines dependency versions without adding them to the project

---

## Step 2: Create Shared Utility Library

### Purpose
Create a reusable library containing common utilities that both services will use.

### Directory Structure
```
utility-library/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/kreasipositif/utility/
        │       ├── config/
        │       ├── formatter/
        │       ├── validator/
        │       └── generator/
        └── resources/
            └── META-INF/spring/
```

### Implementation Steps

**1. Create utility-library/pom.xml:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.kreasipositif</groupId>
        <artifactId>demo-monorepository</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>utility-library</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
    </dependencies>
</project>
```

**2. Create Utility Classes:**

**DateFormatter.java** - Formats dates consistently
```java
@Component
public class DateFormatter {
    private static final DateTimeFormatter DEFAULT_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_FORMATTER);
    }
}
```

**NumberFormatter.java** - Formats numbers and currency
```java
@Component
public class NumberFormatter {
    public String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }
}
```

**StringValidator.java** - Validates strings
```java
@Component
public class StringValidator {
    public boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
```

**IdGenerator.java** - Generates unique IDs
```java
@Component
public class IdGenerator {
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
```

**3. Configure Auto-Configuration:**

Create `UtilityAutoConfiguration.java`:
```java
@Configuration
@ComponentScan(basePackages = "com.kreasipositif.utility")
public class UtilityAutoConfiguration {
}
```

Create `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`:
```
com.kreasipositif.utility.config.UtilityAutoConfiguration
```

### Why This Matters
- `@Component` annotations make utilities auto-injectable
- Auto-configuration ensures utilities are available when the library is added as a dependency
- No manual configuration needed in consuming services

---

## Step 3: Create Service A (User Management)

### Purpose
Create a Spring Boot REST API that manages users and demonstrates utility library usage.

### Implementation

**1. Create service-a/pom.xml:**

```xml
<project>
    <parent>
        <groupId>com.kreasipositif</groupId>
        <artifactId>demo-monorepository</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>service-a</artifactId>

    <dependencies>
        <!-- Shared Utility Library -->
        <dependency>
            <groupId>com.kreasipositif</groupId>
            <artifactId>utility-library</artifactId>
            <version>${project.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
</project>
```

**2. Create Main Application Class:**

```java
@SpringBootApplication
public class ServiceAApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAApplication.class, args);
    }
}
```

**3. Create UserService:**

```java
@Service
@RequiredArgsConstructor
public class UserService {
    private final IdGenerator idGenerator;           // From utility-library
    private final DateFormatter dateFormatter;       // From utility-library
    private final StringValidator stringValidator;   // From utility-library

    public UserResponse createUser(CreateUserRequest request) {
        // Validate using shared validator
        if (!stringValidator.isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email");
        }

        // Generate ID using shared generator
        String userId = idGenerator.generateUUID();
        
        // Format date using shared formatter
        String createdAt = dateFormatter.formatDateTime(LocalDateTime.now());
        
        // ... rest of logic
    }
}
```

**4. Configure application.properties:**

```properties
server.port=8081
spring.application.name=service-a
```

### Utility Usage in Service A
- ✅ `IdGenerator` - Generate user IDs
- ✅ `DateFormatter` - Format timestamps
- ✅ `StringValidator` - Validate email and phone

---

## Step 4: Create Service B (Order Management)

### Purpose
Create another Spring Boot REST API that manages orders, showing how multiple services can share the same utilities.

### Implementation

Similar structure to Service A, but with different business logic:

**1. Create service-b/pom.xml** (same dependency on utility-library)

**2. Create OrderService:**

```java
@Service
@RequiredArgsConstructor
public class OrderService {
    private final IdGenerator idGenerator;
    private final DateFormatter dateFormatter;
    private final NumberFormatter numberFormatter;    // Additional utility
    private final StringValidator stringValidator;

    public OrderResponse createOrder(CreateOrderRequest request) {
        // Generate order ID and number using shared utilities
        String orderId = idGenerator.generateUUID();
        String orderNumber = "ORD-" + idGenerator.generateAlphanumeric(8);
        
        // Format currency using shared formatter
        String totalAmount = numberFormatter.formatCurrency(
            request.getQuantity() * request.getUnitPrice()
        );
        
        // ... rest of logic
    }
}
```

**3. Configure application.properties:**

```properties
server.port=8082
spring.application.name=service-b
```

### Utility Usage in Service B
- ✅ `IdGenerator` - Generate order IDs and order numbers
- ✅ `DateFormatter` - Format timestamps
- ✅ `NumberFormatter` - Format currency and quantities
- ✅ `StringValidator` - Validate input

---

## Step 5: Build and Test

### Build All Modules

From the root directory:

```bash
# Clean and build all modules
mvn clean install
```

**Build Order:**
1. `utility-library` (no dependencies)
2. `service-a` (depends on utility-library)
3. `service-b` (depends on utility-library)

### Verify Build Success

Check for JAR files:
```bash
ls -l utility-library/target/*.jar
ls -l service-a/target/*.jar
ls -l service-b/target/*.jar
```

You should see:
- `utility-library-1.0.0-SNAPSHOT.jar`
- `service-a-1.0.0-SNAPSHOT.jar`
- `service-b-1.0.0-SNAPSHOT.jar`

---

## Step 6: Run the Applications

### Start Service A

**Terminal 1:**
```bash
cd service-a
mvn spring-boot:run
```

Wait for the message:
```
Started ServiceAApplication in X.XXX seconds
```

### Start Service B

**Terminal 2:**
```bash
cd service-b
mvn spring-boot:run
```

### Test Service A (User Management)

**Create a user:**
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890"
  }'
```

**Expected Response:**
```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "createdAt": "2026-01-06 13:15:30",
  "updatedAt": "2026-01-06 13:15:30"
}
```

**Get all users:**
```bash
curl http://localhost:8081/api/users
```

### Test Service B (Order Management)

**Create an order:**
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

**Expected Response:**
```json
{
  "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
  "orderNumber": "ORD-A7B8C9D0",
  "customerId": "CUST-123",
  "productName": "Laptop",
  "quantity": "2",
  "unitPrice": "$999.99",
  "totalAmount": "$1,999.98",
  "createdAt": "2026-01-06 13:16:45",
  "status": "PENDING"
}
```

**Get all orders:**
```bash
curl http://localhost:8082/api/orders
```

---

## Key Observations

### 1. Shared Utility Behavior

Notice that both services:
- Generate IDs in the same UUID format
- Format dates with the same pattern
- Use the same validation rules

### 2. Code Reusability

The same `IdGenerator`, `DateFormatter`, etc. are used by both services without code duplication.

### 3. Version Consistency

All modules share the same version (`1.0.0-SNAPSHOT`) managed by the parent POM.

### 4. Dependency Management

When you update `utility-library`, both services automatically get the changes after `mvn install`.

---

## Benefits Demonstrated

✅ **Single Source of Truth** - Utility logic exists in one place  
✅ **Easy Updates** - Change utility-library, rebuild, all services updated  
✅ **Consistent Behavior** - Both services format/validate identically  
✅ **Simplified Development** - No need for artifact repositories during development  
✅ **Atomic Changes** - Update library and consumers in one commit  

---

## Troubleshooting

### Issue: Service can't find utility classes

**Solution:** Rebuild from root:
```bash
mvn clean install
```

### Issue: Port already in use

**Solution:** Change port in `application.properties` or kill existing process:
```bash
lsof -ti:8081 | xargs kill -9
```

### Issue: Maven build fails

**Solution:** Ensure Java 17+ is being used:
```bash
mvn -version
```

---

## Next Steps

1. **Add Tests** - Create unit tests for utilities and services
2. **Add Database** - Replace in-memory storage with JPA
3. **Add Docker** - Containerize the services
4. **Add CI/CD** - Set up GitHub Actions for automated builds
5. **Add More Utilities** - Expand the utility library with more common features

---

## Conclusion

You now have a fully functional Maven monorepository with:
- ✅ 1 shared utility library
- ✅ 2 Spring Boot microservices
- ✅ Demonstrated code reusability
- ✅ Production-ready structure

This pattern is ideal for:
- Microservices within a single organization
- Projects with shared business logic
- Teams wanting fast development cycles
- Applications requiring consistent behavior

Happy coding! 🚀
