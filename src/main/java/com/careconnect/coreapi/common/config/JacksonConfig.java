package com.careconnect.coreapi.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jackson configuration to handle Hibernate lazy loading and Java 8 time types
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Register JSR310 module for Java 8 time types (Instant, LocalDate, etc.)
        mapper.registerModule(new JavaTimeModule());
        
        // Disable timestamp serialization for dates
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        // Register Hibernate6 module to handle lazy loading
        Hibernate6Module hibernate6Module = new Hibernate6Module();
        
        // This will prevent serialization of lazy-loaded properties that aren't initialized
        hibernate6Module.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false);
        hibernate6Module.configure(Hibernate6Module.Feature.USE_TRANSIENT_ANNOTATION, false);
        
        mapper.registerModule(hibernate6Module);
        
        return mapper;
    }
}
