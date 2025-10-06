#!/bin/bash
# Production deployment script for Link2Care

set -e

echo "🏥 Link2Care Healthcare API Deployment"

# Set production environment variables
export SPRING_PROFILES_ACTIVE=healthcare
export JWT_SECRET=${JWT_SECRET:-$(openssl rand -base64 32)}
export JWT_EXPIRATION=900000  # 15 minutes for healthcare

# JVM optimization for production
JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseStringDeduplication"

echo "Starting healthcare management API..."
java $JAVA_OPTS -jar target/employee-management-api-*.jar

echo "✅ Healthcare API deployed successfully"
echo "🔗 Health check: http://localhost:8080/api/v1/health"
echo "📚 API docs: http://localhost:8080/swagger-ui.html"