# API Testing Guide

Quick reference for testing the monorepository services.

## Service A - User Management (Port 8081)

### Health Check
```bash
curl http://localhost:8081/actuator/health
```

### Create a User
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890"
  }'
```

### Get All Users
```bash
curl http://localhost:8081/api/users
```

### Get User by ID
```bash
# Replace {id} with actual user ID from create response
curl http://localhost:8081/api/users/{id}
```

### Test Validation - Invalid Email
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Invalid User",
    "email": "not-an-email",
    "phone": "+1234567890"
  }'
# Expected: HTTP 400 Bad Request
```

### Test Validation - Invalid Phone
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Invalid User",
    "email": "valid@example.com",
    "phone": "invalid-phone"
  }'
# Expected: HTTP 400 Bad Request
```

---

## Service B - Order Management (Port 8082)

### Health Check
```bash
curl http://localhost:8082/actuator/health
```

### Create an Order
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

### Create Another Order (Different Product)
```bash
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-456",
    "productName": "Smartphone",
    "quantity": 5,
    "unitPrice": 599.50
  }'
```

### Get All Orders
```bash
curl http://localhost:8082/api/orders
```

### Get Order by ID
```bash
# Replace {id} with actual order ID from create response
curl http://localhost:8082/api/orders/{id}
```

### Test Validation - Invalid Quantity
```bash
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-789",
    "productName": "Invalid Product",
    "quantity": -1,
    "unitPrice": 100.00
  }'
# Expected: HTTP 400 Bad Request
```

### Test Validation - Empty Product Name
```bash
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-999",
    "productName": "",
    "quantity": 1,
    "unitPrice": 100.00
  }'
# Expected: HTTP 400 Bad Request
```

---

## Testing Utility Library Features

### DateFormatter (Both Services)
When you create users or orders, notice the `createdAt` field:
- Format: `yyyy-MM-dd HH:mm:ss`
- Example: `2026-01-06 13:15:30`

### IdGenerator (Both Services)
- **Service A**: User IDs are UUIDs
  - Example: `a1b2c3d4-e5f6-7890-abcd-ef1234567890`
- **Service B**: 
  - Order IDs are UUIDs
  - Order Numbers use format: `ORD-{8 alphanumeric}`
  - Example: `ORD-A7B8C9D0`

### NumberFormatter (Service B Only)
Check the order response for formatted numbers:
- `quantity`: `"2"` (formatted with thousand separators for large numbers)
- `unitPrice`: `"$999.99"` (formatted as USD currency)
- `totalAmount`: `"$1,999.98"` (formatted as USD currency)

### StringValidator (Both Services)
Try these to see validation in action:
- Invalid email format → HTTP 400
- Invalid phone format → HTTP 400
- Empty required fields → HTTP 400

---

## Pretty Print JSON Responses

### Using jq (if installed)
```bash
curl http://localhost:8081/api/users | jq
```

### Using Python
```bash
curl http://localhost:8081/api/users | python3 -m json.tool
```

### Using Node.js
```bash
curl http://localhost:8081/api/users | node -e "console.log(JSON.stringify(JSON.parse(require('fs').readFileSync(0)), null, 2))"
```

---

## Automated Testing

Use the provided test scripts:

```bash
# Test Service A
./test-service-a.sh

# Test Service B
./test-service-b.sh
```

---

## Complete Workflow Example

### 1. Build the project
```bash
./build.sh
```

### 2. Start both services (in separate terminals)
```bash
# Terminal 1
./run-service-a.sh

# Terminal 2
./run-service-b.sh
```

### 3. Create a user
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "phone": "+11234567890"
  }' | python3 -m json.tool
```

Save the returned user `id`.

### 4. Create an order for that user
```bash
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-ALICE",
    "productName": "Development Course",
    "quantity": 1,
    "unitPrice": 49.99
  }' | python3 -m json.tool
```

### 5. Verify both services are using shared utilities
Compare the ID formats and date formats between the two services - they should be identical!

---

## Troubleshooting

### Service not responding
Check if it's running:
```bash
# Check Service A
curl http://localhost:8081/actuator/health

# Check Service B
curl http://localhost:8082/actuator/health
```

### Port already in use
Find and kill the process:
```bash
# Find process on port 8081
lsof -ti:8081

# Kill it
lsof -ti:8081 | xargs kill -9

# Same for port 8082
lsof -ti:8082 | xargs kill -9
```

### See all running Java processes
```bash
jps -l
```

---

## Expected Behaviors

### ✅ Both services should:
- Start successfully on their respective ports
- Return valid JSON responses
- Validate input according to utility library rules
- Format dates consistently
- Generate IDs using the same patterns

### ✅ Utility library should provide:
- Consistent UUID generation
- Consistent date formatting
- Consistent validation rules
- Consistent number/currency formatting

This demonstrates the power of a monorepository with shared utilities! 🚀
