package com.careconnect.coreapi.communications;

import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.ApplicationModule;

@Configuration
@ApplicationModule(
    allowedDependencies = {"common", "user"}
)
public class CommunicationsModule {
    // This class serves as the module boundary for Communications management
    // Allows access to User JPA entities for messaging relationships
}
