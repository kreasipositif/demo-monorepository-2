#!/bin/bash

echo "🔄 Restarting backend services with CORS enabled..."

# Kill existing Java processes on ports 8081 and 8082
echo "🛑 Stopping existing services..."
lsof -ti:8081 | xargs kill -9 2>/dev/null
lsof -ti:8082 | xargs kill -9 2>/dev/null

sleep 2

# Start backend services
echo "📦 Starting Service A (port 8081)..."
./nx serve service-a &
SERVICE_A_PID=$!

echo "📦 Starting Service B (port 8082)..."
./nx serve service-b &
SERVICE_B_PID=$!

echo ""
echo "✅ Backend services restarted with CORS enabled!"
echo ""
echo "Service A PID: $SERVICE_A_PID (http://localhost:8081)"
echo "Service B PID: $SERVICE_B_PID (http://localhost:8082)"
echo ""
echo "⏳ Waiting for services to be ready (30 seconds)..."
sleep 30
echo "✅ Services should now be accessible from the frontend!"
echo ""
echo "Press Ctrl+C to stop all services"

# Wait for Ctrl+C
trap "echo ''; echo '🛑 Stopping all services...'; kill $SERVICE_A_PID $SERVICE_B_PID 2>/dev/null; exit" INT

# Keep script running
wait
