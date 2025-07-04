package com.careconnect.coreapi.user.internal;

import java.util.Map;
import java.util.UUID;

import com.careconnect.coreapi.common.response.VerifiedJwtResponse;
import com.careconnect.coreapi.user.UserInfo;
import com.careconnect.coreapi.user.api.RegisterUserCommand;
import com.careconnect.coreapi.user.api.UserManagement;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

/**
 * Internal REST controller for User module operations.
 * Should not be accessed directly by other modules - they should use UserManagement API.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserManagement userManagement;

    @GetMapping("/clerk_jwt")
    public VerifiedJwtResponse clerkJwt(@AuthenticationPrincipal String userId) {
        logger.info("User ID: {}", userId);
        return new VerifiedJwtResponse(userId);
    }

    @GetMapping("/gated_data")
    public Map<String, String> getGatedData() {
        return Map.of("foo", "bar");
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable UUID id) {
        return userManagement.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/clerk/{clerkUserId}")
    public ResponseEntity<UserInfo> getUserByClerkId(@PathVariable String clerkUserId) {
        return userManagement.findUserByClerkId(clerkUserId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public UserInfo registerUser(@RequestBody RegisterUserCommand command) {
        return userManagement.registerUser(command);
    }

}