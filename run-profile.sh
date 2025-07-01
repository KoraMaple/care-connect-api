#!/bin/bash

# Profile switcher script for CareConnect CoreAPI
# Usage: ./run-profile.sh [dev|test|prod]

PROFILE=${1:-dev}

echo "🚀 Starting CareConnect CoreAPI with profile: $PROFILE"

case $PROFILE in
    dev)
        echo "📋 Development Profile:"
        echo "   - Database: H2 in-memory"
        echo "   - Security: Enabled with permissive endpoints"
        echo "   - H2 Console: http://localhost:8080/h2-console"
        echo "   - Port: 8080"
        ;;
    test)
        echo "🧪 Test Profile:"
        echo "   - Database: H2 in-memory"
        echo "   - Security: Disabled"
        echo "   - Port: Random"
        ;;
    prod)
        echo "🏭 Production Profile:"
        echo "   - Database: PostgreSQL (requires env vars)"
        echo "   - Security: Strict"
        echo "   - Port: 8080"
        echo ""
        echo "⚠️  Required Environment Variables:"
        echo "   - DATABASE_URL"
        echo "   - DB_USERNAME"  
        echo "   - DB_PASSWORD"
        echo "   - CLERK_API_SECRET_KEY"
        echo "   - CLERK_AUTHORIZED_PARTIES"
        ;;
    *)
        echo "❌ Invalid profile: $PROFILE"
        echo "Valid profiles: dev, test, prod"
        exit 1
        ;;
esac

echo ""
echo "🔧 Running: mvn spring-boot:run -Dspring-boot.run.profiles=$PROFILE"
echo "📝 To stop: Ctrl+C or kill the process"
echo "=========================================="
echo ""

export SPRING_PROFILES_ACTIVE=$PROFILE
mvn spring-boot:run -Dspring-boot.run.profiles=$PROFILE
