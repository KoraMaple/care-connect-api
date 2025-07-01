package com.careconnect.coreapi.childmgmt.service;

import com.careconnect.coreapi.childmgmt.api.ChildInfo;
import com.careconnect.coreapi.childmgmt.api.ChildManagementService;
import com.careconnect.coreapi.childmgmt.api.GuardianInfo;
import com.careconnect.coreapi.childmgmt.jpa.Child;
import com.careconnect.coreapi.childmgmt.jpa.Guardian;
import com.careconnect.coreapi.childmgmt.repository.ChildRepository;
import com.careconnect.coreapi.childmgmt.repository.GuardianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChildManagementServiceImpl implements ChildManagementService {
    
    private final ChildRepository childRepository;
    private final GuardianRepository guardianRepository;
    
    @Override
    public Optional<ChildInfo> findChildById(UUID childId) {
        return childRepository.findById(childId)
                .map(this::mapToChildInfo);
    }
    
    @Override
    public Optional<GuardianInfo> findGuardianById(UUID guardianId) {
        return guardianRepository.findById(guardianId)
                .map(this::mapToGuardianInfo);
    }
    
    @Override
    public Optional<GuardianInfo> findGuardianByUserId(UUID userId) {
        return guardianRepository.findByUserId(userId)
                .map(this::mapToGuardianInfo);
    }
    
    private ChildInfo mapToChildInfo(Child child) {
        return new ChildInfo(
            child.getId(),
            child.getFirstName(),
            child.getLastName(),
            child.getDob() != null ? child.getDob().toString() : null
        );
    }
    
    private GuardianInfo mapToGuardianInfo(Guardian guardian) {
        return new GuardianInfo(
            guardian.getId(),
            guardian.getRelationship(),
            guardian.getEmergencyContact(),
            guardian.getPickupAuthorized(),
            guardian.getUserId()
        );
    }
}
