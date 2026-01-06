#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Testing Service B (Order Management)${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Check if service is running
if ! curl -s http://localhost:8082/actuator/health > /dev/null; then
    echo -e "${YELLOW}⚠ Service B is not running on port 8082${NC}"
    echo -e "${YELLOW}Please start it first: ./run-service-b.sh${NC}\n"
    exit 1
fi

echo -e "${GREEN}Service B is running!${NC}\n"

# Test 1: Create an order
echo -e "${BLUE}Test 1: Creating an order...${NC}"
response=$(curl -s -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-123",
    "productName": "Laptop",
    "quantity": 2,
    "unitPrice": 999.99
  }')

echo -e "${GREEN}Response:${NC}"
echo "$response" | python3 -m json.tool 2>/dev/null || echo "$response"
echo ""

# Test 2: Create another order
echo -e "${BLUE}Test 2: Creating another order...${NC}"
curl -s -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-456",
    "productName": "Smartphone",
    "quantity": 5,
    "unitPrice": 599.50
  }' | python3 -m json.tool 2>/dev/null
echo ""

# Test 3: Get all orders
echo -e "${BLUE}Test 3: Getting all orders...${NC}"
curl -s http://localhost:8082/api/orders | python3 -m json.tool 2>/dev/null
echo ""

# Test 4: Invalid quantity validation
echo -e "${BLUE}Test 4: Testing validation (invalid quantity)...${NC}"
curl -s -w "\nHTTP Status: %{http_code}\n" -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-789",
    "productName": "Invalid Product",
    "quantity": -1,
    "unitPrice": 100.00
  }'
echo ""

echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}Service B tests completed!${NC}"
echo -e "${BLUE}========================================${NC}\n"
