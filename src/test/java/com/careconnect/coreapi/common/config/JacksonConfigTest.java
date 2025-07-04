package com.careconnect.coreapi.common.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonConfigTest {

    @Test
    void objectMapper_ShouldBeConfiguredCorrectly() {
        // Given
        JacksonConfig jacksonConfig = new JacksonConfig();

        // When
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = jacksonConfig.objectMapper();

        // Then
        assertThat(objectMapper).isNotNull();
        assertThat(objectMapper.getSerializationConfig()).isNotNull();
        assertThat(objectMapper.getDeserializationConfig()).isNotNull();
    }
}
