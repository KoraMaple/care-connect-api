package com.careconnect.coreapi.childmgmt;

import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.ApplicationModule;

@Configuration
@ApplicationModule(
    allowedDependencies = {"common", "user"}
)
public class ChildManagementModule {

    // This class serves as the module boundary for Spring Boot Modulith
    // Allows access to specific User module interfaces for necessary operations
}
