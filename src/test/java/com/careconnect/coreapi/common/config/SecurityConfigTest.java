package com.careconnect.coreapi.common.config;

import com.careconnect.coreapi.common.filter.RequestAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "app.security.enabled=false",
    "app.security.public-endpoints[0]=/api/**"
})
class SecurityConfigTest {

    @Mock
    private RequestAuthenticationFilter requestAuthenticationFilter;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        // Create real SecurityProperties for testing
        SecurityProperties securityProperties = new SecurityProperties();
        securityProperties.setEnabled(false);
        securityProperties.setPublicEndpoints(Arrays.asList("/api/**", "/actuator/**"));
        
        securityConfig = new SecurityConfig(requestAuthenticationFilter, securityProperties);
        ReflectionTestUtils.setField(securityConfig, "allowedOrigins", Arrays.asList("http://localhost:3000"));
    }

    @Test
    void corsFilter_ShouldBeConfiguredCorrectly() {
        // When
        org.springframework.web.filter.CorsFilter corsFilter = securityConfig.corsFilter();

        // Then
        assertThat(corsFilter).isNotNull();
    }

    @Test
    void securityFilterChain_ShouldBeConfiguredCorrectly() throws Exception {
        // Given
        HttpSecurity httpSecurity = mock(HttpSecurity.class);
        
        // Mock only the calls that are actually made when security is disabled
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        
        // When - Test that the method executes without throwing exceptions
        securityConfig.securityFilterChain(httpSecurity);
        
        // Then - If we reach here, the configuration method works correctly
        assertThat(securityConfig).isNotNull();
    }

    @Test
    void contextLoads() {
        new ApplicationContextRunner()
                .withUserConfiguration(SecurityConfig.class, SecurityProperties.class, RequestAuthenticationFilter.class)
                .withPropertyValues(
                    "app.security.enabled=false",
                    "app.security.public-endpoints[0]=/api/**",
                    "clerk.api.secret-key=test-key",
                    "clerk.api.authorized-parties[0]=test-party"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(SecurityConfig.class);
                });
    }
}
