package com.careconnect.coreapi.attendance;

import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.ApplicationModule;

@Configuration
@ApplicationModule(
    allowedDependencies = {"common", "childmgmt::jpa", "facility"}
)
public class AttendanceModule {
    // This class serves as the module boundary for Attendance management
    // Allows access to Child and Facility JPA entities for attendance relationships
}
