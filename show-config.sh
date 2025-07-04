#!/bin/bash

# Configuration validation script
# Shows the differences between profile configurations

echo "🔍 CareConnect CoreAPI - Configuration Overview"
echo "==============================================="
echo ""

echo "📁 Available configuration files:"
ls -la src/main/resources/application*.properties

echo ""
echo "🛡️  Security Configuration by Profile:"
echo ""

echo "🔧 DEFAULT (application.properties):"
grep -E "app\.security\.(enabled|public-endpoints)" src/main/resources/application.properties | sed 's/^/   /'

echo ""
echo "🚀 DEV Profile (application-dev.properties):"
grep -E "app\.security\.(enabled|public-endpoints)" src/main/resources/application-dev.properties | sed 's/^/   /'

echo ""
echo "🧪 TEST Profile (application-test.properties):"
grep -E "app\.security\.(enabled|public-endpoints)" src/main/resources/application-test.properties | sed 's/^/   /'

echo ""
echo "🏭 PROD Profile (application-prod.properties):"
grep -E "app\.security\.(enabled|public-endpoints)" src/main/resources/application-prod.properties | sed 's/^/   /'

echo ""
echo "💾 Database Configuration by Profile:"
echo ""

echo "🔧 DEFAULT:"
grep -E "spring\.datasource\.(driver-class-name|url)" src/main/resources/application.properties | head -2 | sed 's/^/   /'

echo ""
echo "🚀 DEV:"
grep -E "spring\.datasource\.(driver-class-name|url)" src/main/resources/application-dev.properties | head -2 | sed 's/^/   /'

echo ""
echo "🧪 TEST:"
grep -E "spring\.datasource\.(driver-class-name|url)" src/main/resources/application-test.properties | head -2 | sed 's/^/   /'

echo ""
echo "🏭 PROD:"
grep -E "spring\.datasource\.(driver-class-name|url)" src/main/resources/application-prod.properties | head -2 | sed 's/^/   /'

echo ""
echo "🎯 Current Active Profile:"
if [ -n "$SPRING_PROFILES_ACTIVE" ]; then
    echo "   SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE"
else
    echo "   No profile set (will use default)"
fi

echo ""
echo "📖 Usage Examples:"
echo "   ./run-profile.sh dev    # Start with development profile"
echo "   ./run-profile.sh test   # Start with test profile  "
echo "   ./run-profile.sh prod   # Start with production profile"
echo ""
echo "   export SPRING_PROFILES_ACTIVE=dev && java -jar target/coreapi.jar"
echo "   java -jar target/coreapi.jar --spring.profiles.active=prod"
