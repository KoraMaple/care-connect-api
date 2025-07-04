package com.careconnect.coreapi.childmgmt.internal.controller;

import com.careconnect.coreapi.childmgmt.domain.Child;
import com.careconnect.coreapi.childmgmt.dto.ChildRequestDto;
import com.careconnect.coreapi.childmgmt.internal.service.ChildGuardianService;
import com.careconnect.coreapi.childmgmt.internal.service.ChildService;
import com.careconnect.coreapi.common.response.PageResponse;
import com.careconnect.coreapi.common.config.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

@WebMvcTest(value = ChildController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ActiveProfiles("test")
class ChildControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChildService childService;

    @MockitoBean
    private ChildGuardianService childGuardianService;

    @MockitoBean
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;

    private Child testChild;
    private ChildRequestDto testChildRequest;
    private UUID childId;

    @BeforeEach
    void setUp() {
        childId = UUID.randomUUID();
        
        testChild = new Child();
        testChild.setId(childId);
        testChild.setFirstName("John");
        testChild.setLastName("Doe");
        testChild.setDob(Instant.parse("2020-01-01T00:00:00Z"));
        testChild.setGender("Male");
        testChild.setCreatedAt(Instant.now());
        testChild.setUpdatedAt(Instant.now());

        testChildRequest = new ChildRequestDto();
        testChildRequest.setFirstName("John");
        testChildRequest.setLastName("Doe");
        testChildRequest.setDob(Instant.parse("2020-01-01T00:00:00Z"));
        testChildRequest.setGender("Male");
    }

    @Test
    void getAllChildren_ShouldReturnPageOfChildren() throws Exception {
        // Given
        List<Child> children = Collections.singletonList(testChild);
        PageResponse<Child> pageResponse = PageResponse.of(new PageImpl<>(children, PageRequest.of(0, 20), 1));
        when(childService.getAllChildren(any(Pageable.class))).thenReturn(pageResponse);

        // When & Then
        mockMvc.perform(get("/api/children")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].firstName").value("John"))
                .andExpect(jsonPath("$.data[0].lastName").value("Doe"));

        verify(childService).getAllChildren(any(Pageable.class));
    }

    @Test
    void getChildById_WhenChildExists_ShouldReturnChild() throws Exception {
        // Given
        when(childService.getChildById(childId)).thenReturn(testChild);

        // When & Then
        mockMvc.perform(get("/api/children/{id}", childId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.lastName").value("Doe"))
                .andExpect(jsonPath("$.message").value("Child retrieved successfully"));

        verify(childService).getChildById(childId);
    }

    @Test
    void createChild_WithValidData_ShouldCreateChild() throws Exception {
        // Given
        when(childService.createChild(any(Child.class))).thenReturn(testChild);

        // When & Then
        mockMvc.perform(post("/api/children")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testChildRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.message").value("Child created successfully"));

        verify(childService).createChild(any(Child.class));
    }

    @Test
    void createChild_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        ChildRequestDto invalidRequest = new ChildRequestDto();
        // Leave required fields empty

        // When & Then
        mockMvc.perform(post("/api/children")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(childService, never()).createChild(any(Child.class));
    }

    @Test
    void updateChild_WhenChildExists_ShouldUpdateChild() throws Exception {
        // Given
        when(childService.getChildById(childId)).thenReturn(testChild);
        when(childService.updateChild(eq(childId), any(Child.class))).thenReturn(testChild);

        // When & Then
        mockMvc.perform(put("/api/children/{id}", childId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testChildRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.message").value("Child updated successfully"));

        verify(childService).getChildById(childId);
        verify(childService).updateChild(eq(childId), any(Child.class));
    }

    @Test
    void deleteChild_WhenChildExists_ShouldDeleteChild() throws Exception {
        // Given
        doNothing().when(childService).deleteChild(childId);

        // When & Then
        mockMvc.perform(delete("/api/children/{id}", childId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Child deleted successfully"));

        verify(childService).deleteChild(childId);
    }

    @Test
    void searchChildren_ShouldReturnFilteredResults() throws Exception {
        // Given
        Page<Child> childrenPage = new PageImpl<>(Collections.singletonList(testChild));
        when(childService.searchChildren(eq("John"), eq("Male"), any(Pageable.class)))
                .thenReturn(childrenPage);

        // When & Then
        mockMvc.perform(get("/api/children/search")
                .param("name", "John")
                .param("gender", "Male")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].firstName").value("John"));

        verify(childService).searchChildren(eq("John"), eq("Male"), any(Pageable.class));

    }

}
