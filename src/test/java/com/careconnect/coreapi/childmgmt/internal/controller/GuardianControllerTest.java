package com.careconnect.coreapi.childmgmt.internal.controller;

import com.careconnect.coreapi.childmgmt.domain.Guardian;
import com.careconnect.coreapi.childmgmt.internal.service.GuardianService;
import com.careconnect.coreapi.common.config.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = GuardianController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ActiveProfiles("test")
class GuardianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GuardianService guardianService;

    @MockitoBean
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;

    private Guardian testGuardian;
    private UUID guardianId;

    @BeforeEach
    void setUp() {
        guardianId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        testGuardian = new Guardian();
        testGuardian.setId(guardianId);
        testGuardian.setUserId(userId);
        testGuardian.setRelationship("Parent");
        testGuardian.setPickupAuthorized(true);
        testGuardian.setEmergencyContact("555-1234");
        testGuardian.setCreatedAt(Instant.now());
        testGuardian.setUpdatedAt(Instant.now());
    }

    @Test
    void getAllGuardians_ShouldReturnListOfGuardians() throws Exception {
        // Given
        List<Guardian> guardians = Collections.singletonList(testGuardian);
        when(guardianService.getAllGuardians()).thenReturn(guardians);

        // When & Then
        mockMvc.perform(get("/api/guardians"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].relationship").value("Parent"))
                .andExpect(jsonPath("$.message").value("Guardians retrieved successfully"));

        verify(guardianService).getAllGuardians();
    }

    @Test
    void getGuardianById_WhenGuardianExists_ShouldReturnGuardian() throws Exception {
        // Given
        when(guardianService.getGuardianById(guardianId)).thenReturn(testGuardian);

        // When & Then
        mockMvc.perform(get("/api/guardians/{id}", guardianId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.relationship").value("Parent"))
                .andExpect(jsonPath("$.data.pickupAuthorized").value(true))
                .andExpect(jsonPath("$.message").value("Guardian retrieved successfully"));

        verify(guardianService).getGuardianById(guardianId);
    }

    @Test
    void createGuardian_WithValidData_ShouldCreateGuardian() throws Exception {
        // Given
        when(guardianService.createGuardian(any(Guardian.class))).thenReturn(testGuardian);

        // When & Then
        mockMvc.perform(post("/api/guardians")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testGuardian)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.relationship").value("Parent"))
                .andExpect(jsonPath("$.message").value("Guardian created successfully"));

        verify(guardianService).createGuardian(any(Guardian.class));
    }

    @Test
    void updateGuardian_WhenGuardianExists_ShouldUpdateGuardian() throws Exception {
        // Given
        when(guardianService.updateGuardian(eq(guardianId), any(Guardian.class))).thenReturn(testGuardian);

        // When & Then
        mockMvc.perform(put("/api/guardians/{id}", guardianId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testGuardian)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.relationship").value("Parent"))
                .andExpect(jsonPath("$.message").value("Guardian updated successfully"));

        verify(guardianService).updateGuardian(eq(guardianId), any(Guardian.class));
    }

    @Test
    void deleteGuardian_WhenGuardianExists_ShouldDeleteGuardian() throws Exception {
        // Given
        doNothing().when(guardianService).deleteGuardian(guardianId);

        // When & Then
        mockMvc.perform(delete("/api/guardians/{id}", guardianId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Guardian deleted successfully"));

        verify(guardianService).deleteGuardian(guardianId);
    }
}
