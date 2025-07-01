package com.careconnect.coreapi.childmgmt.internal;

import com.careconnect.coreapi.childmgmt.jpa.Child;
import com.careconnect.coreapi.childmgmt.jpa.ChildGuardian;
import com.careconnect.coreapi.childmgmt.jpa.Guardian;
import com.careconnect.coreapi.childmgmt.service.ChildGuardianService;
import com.careconnect.coreapi.childmgmt.service.ChildService;
import com.careconnect.coreapi.common.response.ApiResponse;
import com.careconnect.coreapi.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/children")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;
    private final ChildGuardianService childGuardianService;

    @GetMapping
    public PageResponse<Child> getAllChildren(
            @PageableDefault(size = 20) @RequestParam int pageNumber) {
        log.info("GET /api/children - Fetching all children with pagination");

        return this.childService.getAllChildren(pageNumber, 20);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Child>> getChildById(@PathVariable UUID id) {
        log.info("GET /api/children/{} - Fetching child by ID", id);

        Child child = childService.getChildById(id);
        return ResponseEntity.ok(ApiResponse.success(child, "Child retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Child>> createChild(@Valid @RequestBody Child child) {
        log.info("POST /api/children - Creating new child: {} {}",
                child.getFirstName(), child.getLastName());

        Child createdChild = childService.createChild(child);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdChild, "Child created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Child>> updateChild(
            @PathVariable UUID id,
            @Valid @RequestBody Child child) {
        log.info("PUT /api/children/{} - Updating child", id);

        Child updatedChild = childService.updateChild(id, child);
        return ResponseEntity.ok(ApiResponse.success(updatedChild, "Child updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteChild(@PathVariable UUID id) {
        log.info("DELETE /api/children/{} - Deleting child", id);

        childService.deleteChild(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Child deleted successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Child>>> searchChildren(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/children/search - Searching children with criteria");

        Page<Child> children = childService.searchChildren(name, gender, pageable);
        return ResponseEntity.ok(ApiResponse.success(children, "Children search completed successfully"));
    }

    @PostMapping("/{childId}/guardians")
    public ResponseEntity<ApiResponse<ChildGuardian>> addGuardianToChild(
            @PathVariable UUID childId,
            @RequestParam UUID guardianId,
            @RequestParam(defaultValue = "false") Boolean isPrimary) {
        log.info("POST /api/children/{}/guardians - Adding guardian {} to child", childId, guardianId);

        ChildGuardian relationship = childGuardianService.addGuardianToChild(childId, guardianId, isPrimary);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(relationship, "Guardian added to child successfully"));
    }

    @GetMapping("/{childId}/guardians")
    public ResponseEntity<ApiResponse<List<Guardian>>> getGuardiansForChild(@PathVariable UUID childId) {
        log.info("GET /api/children/{}/guardians - Fetching guardians for child", childId);

        List<Guardian> guardians = childGuardianService.getGuardiansForChild(childId);
        return ResponseEntity.ok(ApiResponse.success(guardians, "Guardians retrieved successfully"));
    }

    @DeleteMapping("/{childId}/guardians/{guardianId}")
    public ResponseEntity<ApiResponse<Void>> removeGuardianFromChild(
            @PathVariable UUID childId,
            @PathVariable UUID guardianId) {
        log.info("DELETE /api/children/{}/guardians/{} - Removing guardian from child", childId, guardianId);

        childGuardianService.removeGuardianFromChild(childId, guardianId);
        return ResponseEntity.ok(ApiResponse.success(null, "Guardian removed from child successfully"));
    }

    @PutMapping("/{childId}/guardians/{guardianId}/primary")
    public ResponseEntity<ApiResponse<ChildGuardian>> setPrimaryGuardian(
            @PathVariable UUID childId,
            @PathVariable UUID guardianId) {
        log.info("PUT /api/children/{}/guardians/{}/primary - Setting primary guardian", childId, guardianId);

        ChildGuardian relationship = childGuardianService.updatePrimaryGuardian(childId, guardianId);
        return ResponseEntity.ok(ApiResponse.success(relationship, "Primary guardian updated successfully"));
    }
}
