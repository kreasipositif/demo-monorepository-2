#!/bin/bash

echo "🚀 Starting all services in order..."

# Start backend services first
echo "📦 Starting Service A (port 8081)..."
./nx serve service-a &
SERVICE_A_PID=$!

echo "📦 Starting Service B (port 8082)..."
./nx serve service-b &
SERVICE_B_PID=$!

# Wait for backends to be ready
echo "⏳ Waiting 30 seconds for backend services to start..."
sleep 30

# Start frontend
echo "🎨 Starting Frontend (port 3000)..."
./nx serve frontend &
FRONTEND_PID=$!

echo ""
echo "✅ All services started!"
echo ""
echo "Service A PID: $SERVICE_A_PID (http://localhost:8081)"
echo "Service B PID: $SERVICE_B_PID (http://localhost:8082)"
echo "Frontend PID:  $FRONTEND_PID (http://localhost:3000)"
echo ""
echo "Press Ctrl+C to stop all services"

# Wait for Ctrl+C
trap "echo ''; echo '🛑 Stopping all services...'; kill $SERVICE_A_PID $SERVICE_B_PID $FRONTEND_PID 2>/dev/null; exit" INT

# Keep script running
wait
