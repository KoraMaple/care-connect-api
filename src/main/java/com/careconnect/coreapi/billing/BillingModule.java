package com.careconnect.coreapi.billing;

import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.ApplicationModule;

@Configuration
@ApplicationModule(
    allowedDependencies = {"common", "childmgmt::jpa"}
)
public class BillingModule {
    // This class serves as the module boundary for Billing management
    // Allows access to Child Management JPA entities for billing relationships
}
