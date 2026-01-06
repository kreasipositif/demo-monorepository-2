#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Building Demo Monorepository${NC}"
echo -e "${BLUE}========================================${NC}"

# Build all modules
echo -e "\n${GREEN}Step 1: Building all modules...${NC}"
mvn clean install

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Build successful!${NC}"
else
    echo -e "${RED}✗ Build failed!${NC}"
    exit 1
fi

echo -e "\n${BLUE}========================================${NC}"
echo -e "${BLUE}Build Summary${NC}"
echo -e "${BLUE}========================================${NC}"

# Check for JAR files
if [ -f "utility-library/target/utility-library-1.0.0-SNAPSHOT.jar" ]; then
    echo -e "${GREEN}✓ utility-library built successfully${NC}"
else
    echo -e "${RED}✗ utility-library build failed${NC}"
fi

if [ -f "service-a/target/service-a-1.0.0-SNAPSHOT.jar" ]; then
    echo -e "${GREEN}✓ service-a built successfully${NC}"
else
    echo -e "${RED}✗ service-a build failed${NC}"
fi

if [ -f "service-b/target/service-b-1.0.0-SNAPSHOT.jar" ]; then
    echo -e "${GREEN}✓ service-b built successfully${NC}"
else
    echo -e "${RED}✗ service-b build failed${NC}"
fi

echo -e "\n${BLUE}========================================${NC}"
echo -e "${GREEN}Next Steps:${NC}"
echo -e "${BLUE}========================================${NC}"
echo -e "Run Service A (User Management):"
echo -e "  ${GREEN}cd service-a && mvn spring-boot:run${NC}"
echo -e ""
echo -e "Run Service B (Order Management):"
echo -e "  ${GREEN}cd service-b && mvn spring-boot:run${NC}"
echo -e ""
echo -e "Or use the provided run scripts:"
echo -e "  ${GREEN}./run-service-a.sh${NC}"
echo -e "  ${GREEN}./run-service-b.sh${NC}"
echo -e ""
