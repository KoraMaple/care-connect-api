package com.careconnect.coreapi.childmgmt.internal;

import com.careconnect.coreapi.childmgmt.domain.Child;
import com.careconnect.coreapi.childmgmt.domain.ChildGuardian;
import com.careconnect.coreapi.childmgmt.domain.Guardian;
import com.careconnect.coreapi.childmgmt.dto.ChildRequestDto;
import com.careconnect.coreapi.childmgmt.dto.ChildResponseDto;
import com.careconnect.coreapi.common.response.ApiResponse;
import com.careconnect.coreapi.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public PageResponse<ChildResponseDto> getAllChildren(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/children - Fetching all children with pagination: page={}, size={}", 
                pageable.getPageNumber(), pageable.getPageSize());

        // Get the service result and check if it returns PageResponse or Page
        PageResponse<Child> childrenPageResponse = this.childService.getAllChildren(pageable);
        
        // Convert to safe DTOs
        List<ChildResponseDto> safeChildren = ChildResponseDto.fromEntityList(childrenPageResponse.getData());
        
        return PageResponse.<ChildResponseDto>builder()
                .data(safeChildren)
                .meta(childrenPageResponse.getMeta())
                .links(childrenPageResponse.getLinks())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChildResponseDto>> getChildById(@PathVariable UUID id) {
        log.info("GET /api/children/{} - Fetching child by ID", id);

        Child child = childService.getChildById(id);
        ChildResponseDto safeChild = ChildResponseDto.fromEntity(child);
        return ResponseEntity.ok(ApiResponse.success(safeChild, "Child retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChildResponseDto>> createChild(@Valid @RequestBody ChildRequestDto childRequest) {
        log.info("POST /api/children - Creating new child: {} {}",
                childRequest.getFirstName(), childRequest.getLastName());

        // Convert sanitized DTO to entity
        Child child = childRequest.toEntity();
        Child createdChild = childService.createChild(child);
        ChildResponseDto safeChild = ChildResponseDto.fromEntity(createdChild);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(safeChild, "Child created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChildResponseDto>> updateChild(
            @PathVariable UUID id,
            @Valid @RequestBody ChildRequestDto childRequest) {
        log.info("PUT /api/children/{} - Updating child", id);

        // Get existing child and update with sanitized data
        Child existingChild = childService.getChildById(id);
        Child updatedData = childRequest.updateEntity(existingChild);
        Child updatedChild = childService.updateChild(id, updatedData);
        ChildResponseDto safeChild = ChildResponseDto.fromEntity(updatedChild);
        return ResponseEntity.ok(ApiResponse.success(safeChild, "Child updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteChild(@PathVariable UUID id) {
        log.info("DELETE /api/children/{} - Deleting child", id);

        childService.deleteChild(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Child deleted successfully"));
    }

    @GetMapping("/search")
    public PageResponse<ChildResponseDto> searchChildren(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/children/search - Searching children with criteria: name={}, gender={}", 
                name, gender);

        Page<Child> children = childService.searchChildren(name, gender, pageable);
        
        // Convert the Spring Page content to safe DTOs and create a new PageResponse
        Page<ChildResponseDto> safePage = children.map(ChildResponseDto::fromEntity);
        
        return PageResponse.of(safePage);
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
