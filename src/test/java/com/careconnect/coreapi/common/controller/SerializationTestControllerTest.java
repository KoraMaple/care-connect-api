package com.careconnect.coreapi.common.controller;

import com.careconnect.coreapi.common.config.SecurityProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value=SerializationTestController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ActiveProfiles("test")
class SerializationTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SecurityProperties securityProperties;

    @Test
    void testJacksonSerialization_ShouldReturnJsonResponse() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/test/jackson"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Jackson serialization test"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testInstantSerialization_ShouldReturnTestResponse() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/test/instant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Instant serialization test"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
