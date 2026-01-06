#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Nx Affected Command Demo${NC}"
echo -e "${BLUE}========================================${NC}\n"

echo -e "${GREEN}This demo shows how Nx intelligently detects${NC}"
echo -e "${GREEN}which projects are affected by changes.${NC}\n"

# Show all projects
echo -e "${BLUE}Step 1: All projects in the workspace${NC}"
./nx show projects
echo ""

# Current git status
echo -e "${BLUE}Step 2: Current git status${NC}"
git status --short
echo ""

# Show affected projects (should be none if no changes)
echo -e "${BLUE}Step 3: Currently affected projects${NC}"
AFFECTED=$(./nx show projects --affected)
if [ -z "$AFFECTED" ]; then
    echo -e "${GREEN}No projects affected (no changes detected)${NC}"
else
    echo -e "${YELLOW}Affected projects:${NC}"
    echo "$AFFECTED"
fi
echo ""

# Simulate change to service-a
echo -e "${BLUE}Step 4: Simulating change to service-a...${NC}"
echo "// Nx demo comment" >> service-a/src/main/java/com/kreasipositif/servicea/ServiceAApplication.java
echo -e "${GREEN}Added comment to ServiceAApplication.java${NC}"
echo ""

# Show affected projects after change
echo -e "${BLUE}Step 5: Affected projects after change${NC}"
echo -e "${YELLOW}Projects affected by service-a change:${NC}"
./nx show projects --affected
echo ""

# Explain what happened
echo -e "${BLUE}Step 6: Explanation${NC}"
echo -e "${YELLOW}Only service-a is affected!${NC}"
echo -e "${GREEN}✓ service-b is NOT affected (won't be built/tested)${NC}"
echo -e "${GREEN}✓ utility-library is NOT affected${NC}"
echo -e "${GREEN}✓ This saves time by building only what changed!${NC}"
echo ""

# Show what commands would run
echo -e "${BLUE}Step 7: Commands that would run${NC}"
echo -e "${YELLOW}To build only affected projects:${NC}"
echo -e "  ${GREEN}./nx affected -t build${NC}"
echo ""
echo -e "${YELLOW}To test only affected projects:${NC}"
echo -e "  ${GREEN}./nx affected -t test${NC}"
echo ""

# Revert the change
echo -e "${BLUE}Step 8: Reverting demo change...${NC}"
git checkout service-a/src/main/java/com/kreasipositif/servicea/ServiceAApplication.java
echo -e "${GREEN}Change reverted${NC}"
echo ""

# Now simulate change to utility-library
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Scenario 2: Change to utility-library${NC}"
echo -e "${BLUE}========================================${NC}\n"

echo -e "${BLUE}Step 9: Simulating change to utility-library...${NC}"
echo "// Nx demo comment" >> utility-library/src/main/java/com/kreasipositif/utility/formatter/DateFormatter.java
echo -e "${GREEN}Added comment to DateFormatter.java${NC}"
echo ""

# Show affected projects after utility change
echo -e "${BLUE}Step 10: Affected projects after utility-library change${NC}"
echo -e "${YELLOW}Projects affected by utility-library change:${NC}"
./nx show projects --affected
echo ""

# Explain what happened
echo -e "${BLUE}Step 11: Explanation${NC}"
echo -e "${YELLOW}ALL projects are affected!${NC}"
echo -e "${GREEN}✓ utility-library changed${NC}"
echo -e "${GREEN}✓ service-a depends on utility-library${NC}"
echo -e "${GREEN}✓ service-b depends on utility-library${NC}"
echo -e "${GREEN}✓ Both services must be rebuilt!${NC}"
echo ""

# Revert the change
echo -e "${BLUE}Step 12: Reverting demo change...${NC}"
git checkout utility-library/src/main/java/com/kreasipositif/utility/formatter/DateFormatter.java
echo -e "${GREEN}Change reverted${NC}"
echo ""

# Summary
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Summary${NC}"
echo -e "${BLUE}========================================${NC}\n"

echo -e "${GREEN}Nx Affected Analysis:${NC}"
echo -e "  • Analyzes git changes"
echo -e "  • Understands project dependencies"
echo -e "  • Builds/tests only what's affected"
echo -e "  • Saves time in development and CI/CD"
echo ""

echo -e "${YELLOW}Key Commands:${NC}"
echo -e "  ${GREEN}./nx show projects --affected${NC}      - List affected projects"
echo -e "  ${GREEN}./nx affected -t build${NC}             - Build affected projects"
echo -e "  ${GREEN}./nx affected -t test${NC}              - Test affected projects"
echo -e "  ${GREEN}./nx graph --affected${NC}              - Visualize affected graph"
echo ""

echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}Demo complete!${NC}"
echo -e "${BLUE}========================================${NC}\n"
