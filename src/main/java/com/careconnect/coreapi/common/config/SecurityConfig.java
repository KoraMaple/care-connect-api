package com.careconnect.coreapi.common.config;

import com.careconnect.coreapi.common.filter.RequestAuthenticationFilter;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final RequestAuthenticationFilter requestAuthenticationFilter;
    private final SecurityProperties securityProperties;

    @Value("${app.allowed-origins}")
    private List<String> allowedOrigins;

    public SecurityConfig(RequestAuthenticationFilter requestAuthenticationFilter, 
                         SecurityProperties securityProperties) {
        this.requestAuthenticationFilter = requestAuthenticationFilter;
        this.securityProperties = securityProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(Customizer.withDefaults());
        
        if (!securityProperties.isEnabled()) {
            // Completely disable security for testing
            httpSecurity
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable());
        } else {
            // Configure with public endpoints from properties
            httpSecurity
                .authorizeHttpRequests(authorizeRequests -> {
                    var authConfig = authorizeRequests;
                    
                    // Add public endpoints from configuration
                    if (!securityProperties.getPublicEndpoints().isEmpty()) {
                        String[] publicEndpoints = securityProperties.getPublicEndpoints()
                            .toArray(new String[0]);
                        authConfig = authConfig.requestMatchers(publicEndpoints).permitAll();
                    }
                    
                    // All other requests require authentication
                    authConfig.anyRequest().authenticated();
                })
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(requestAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }
        
        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        allowedOrigins.forEach(config::addAllowedOrigin);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}