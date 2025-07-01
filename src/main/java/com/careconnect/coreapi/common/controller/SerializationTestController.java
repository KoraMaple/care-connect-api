package com.careconnect.coreapi.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Test controller to verify Jackson serialization works correctly
 */
@RestController
@RequestMapping("/api/test")
public class SerializationTestController {

    @GetMapping("/jackson")
    public Map<String, Object> testJacksonSerialization() {
        return Map.of(
            "message", "Jackson serialization test",
            "timestamp", Instant.now(),
            "success", true
        );
    }
    
    @GetMapping("/instant")  
    public TestResponse testInstantSerialization() {
        return new TestResponse("Instant serialization test", Instant.now());
    }
    
    public static class TestResponse {
        private String message;
        private Instant timestamp;
        
        public TestResponse(String message, Instant timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }
        
        public String getMessage() {
            return message;
        }
        
        public Instant getTimestamp() {
            return timestamp;
        }
    }
}
