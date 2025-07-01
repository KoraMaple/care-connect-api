package com.careconnect.coreapi.childmgmt.internal;

import com.careconnect.coreapi.childmgmt.jpa.Allergy;
import com.careconnect.coreapi.childmgmt.jpa.ChildAllergy;
import com.careconnect.coreapi.childmgmt.service.AllergyService;
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
@RequestMapping("/api/allergies")
@RequiredArgsConstructor
public class AllergyController {

    private final AllergyService allergyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Allergy>>> getAllAllergies() {
        log.info("GET /api/allergies - Fetching all allergies");

        List<Allergy> allergies = allergyService.getAllAllergies();
        return ResponseEntity.ok(ApiResponse.success(allergies, "Allergies retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Allergy>> getAllergyById(@PathVariable UUID id) {
        log.info("GET /api/allergies/{} - Fetching allergy by ID", id);

        Allergy allergy = allergyService.getAllergyById(id);
        return ResponseEntity.ok(ApiResponse.success(allergy, "Allergy retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Allergy>> createAllergy(@Valid @RequestBody Allergy allergy) {
        log.info("POST /api/allergies - Creating new allergy: {}", allergy.getName());

        Allergy createdAllergy = allergyService.createAllergy(allergy);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdAllergy, "Allergy created successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Allergy>>> searchAllergies(@RequestParam String name) {
        log.info("GET /api/allergies/search - Searching allergies by name: {}", name);

        List<Allergy> allergies = allergyService.searchAllergiesByName(name);
        return ResponseEntity.ok(ApiResponse.success(allergies, "Allergy search completed successfully"));
    }

    @PostMapping("/children/{childId}")
    public ResponseEntity<ApiResponse<ChildAllergy>> addAllergyToChild(
            @PathVariable UUID childId,
            @RequestParam UUID allergyId,
            @RequestParam(required = false) String notes) {
        log.info("POST /api/allergies/children/{} - Adding allergy {} to child", childId, allergyId);

        ChildAllergy childAllergy = allergyService.addAllergyToChild(childId, allergyId, notes);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(childAllergy, "Allergy added to child successfully"));
    }

    @GetMapping("/children/{childId}")
    public ResponseEntity<ApiResponse<List<ChildAllergy>>> getAllergiesForChild(@PathVariable UUID childId) {
        log.info("GET /api/allergies/children/{} - Fetching allergies for child", childId);

        List<ChildAllergy> allergies = allergyService.getAllergiesForChild(childId);
        return ResponseEntity.ok(ApiResponse.success(allergies, "Child allergies retrieved successfully"));
    }

    @DeleteMapping("/children/{childId}/{allergyId}")
    public ResponseEntity<ApiResponse<Void>> removeAllergyFromChild(
            @PathVariable UUID childId,
            @PathVariable UUID allergyId) {
        log.info("DELETE /api/allergies/children/{}/{} - Removing allergy from child", childId, allergyId);

        allergyService.removeAllergyFromChild(childId, allergyId);
        return ResponseEntity.ok(ApiResponse.success(null, "Allergy removed from child successfully"));
    }
}
