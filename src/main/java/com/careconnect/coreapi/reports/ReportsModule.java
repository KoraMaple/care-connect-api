package com.careconnect.coreapi.reports;

import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.ApplicationModule;

@Configuration
@ApplicationModule(
    allowedDependencies = {"common"}
)
public class ReportsModule {
    // This class serves as the module boundary for Reports management
}
