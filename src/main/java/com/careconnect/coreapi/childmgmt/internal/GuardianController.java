package com.careconnect.coreapi.childmgmt.controller;

import com.careconnect.coreapi.childmgmt.jpa.Guardian;
import com.careconnect.coreapi.childmgmt.service.GuardianService;
import com.careconnect.coreapi.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/guardians")
@RequiredArgsConstructor
public class GuardianController {

    private final GuardianService guardianService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Guardian>>> getAllGuardians() {
        log.info("GET /api/guardians - Fetching all guardians");

        List<Guardian> guardians = guardianService.getAllGuardians();
        return ResponseEntity.ok(ApiResponse.success(guardians, "Guardians retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Guardian>> getGuardianById(@PathVariable UUID id) {
        log.info("GET /api/guardians/{} - Fetching guardian by ID", id);

        Guardian guardian = guardianService.getGuardianById(id);
        return ResponseEntity.ok(ApiResponse.success(guardian, "Guardian retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Guardian>> createGuardian(@Valid @RequestBody Guardian guardian) {
        log.info("POST /api/guardians - Creating new guardian for user ID: {}", guardian.getUserId());

        Guardian createdGuardian = guardianService.createGuardian(guardian);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdGuardian, "Guardian created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Guardian>> updateGuardian(
            @PathVariable UUID id,
            @Valid @RequestBody Guardian guardian) {
        log.info("PUT /api/guardians/{} - Updating guardian", id);

        Guardian updatedGuardian = guardianService.updateGuardian(id, guardian);
        return ResponseEntity.ok(ApiResponse.success(updatedGuardian, "Guardian updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGuardian(@PathVariable UUID id) {
        log.info("DELETE /api/guardians/{} - Deleting guardian", id);

        guardianService.deleteGuardian(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Guardian deleted successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Guardian>> getGuardianByUserId(@PathVariable UUID userId) {
        log.info("GET /api/guardians/user/{} - Fetching guardian by user ID", userId);

        Guardian guardian = guardianService.getGuardianByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(guardian, "Guardian retrieved successfully"));
    }
}
