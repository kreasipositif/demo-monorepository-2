#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Testing Service A (User Management)${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Check if service is running
if ! curl -s http://localhost:8081/actuator/health > /dev/null; then
    echo -e "${YELLOW}⚠ Service A is not running on port 8081${NC}"
    echo -e "${YELLOW}Please start it first: ./run-service-a.sh${NC}\n"
    exit 1
fi

echo -e "${GREEN}Service A is running!${NC}\n"

# Test 1: Create a user
echo -e "${BLUE}Test 1: Creating a user...${NC}"
response=$(curl -s -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890"
  }')

echo -e "${GREEN}Response:${NC}"
echo "$response" | python3 -m json.tool 2>/dev/null || echo "$response"
echo ""

# Test 2: Create another user
echo -e "${BLUE}Test 2: Creating another user...${NC}"
curl -s -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "phone": "+9876543210"
  }' | python3 -m json.tool 2>/dev/null
echo ""

# Test 3: Get all users
echo -e "${BLUE}Test 3: Getting all users...${NC}"
curl -s http://localhost:8081/api/users | python3 -m json.tool 2>/dev/null
echo ""

# Test 4: Invalid email validation
echo -e "${BLUE}Test 4: Testing validation (invalid email)...${NC}"
curl -s -w "\nHTTP Status: %{http_code}\n" -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Invalid User",
    "email": "invalid-email",
    "phone": "+1234567890"
  }'
echo ""

echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}Service A tests completed!${NC}"
echo -e "${BLUE}========================================${NC}\n"
