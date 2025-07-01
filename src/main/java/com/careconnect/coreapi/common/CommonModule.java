package com.careconnect.coreapi.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.ApplicationModule;

@Configuration
@ApplicationModule(
    type = ApplicationModule.Type.OPEN // Common module should be open to all modules
)
public class CommonModule {
    // This class serves as the module boundary for common utilities
    // All common components are exposed to other modules
}
