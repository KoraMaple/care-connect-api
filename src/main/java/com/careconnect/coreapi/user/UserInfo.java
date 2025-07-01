package com.careconnect.coreapi.user;

import java.util.UUID;

/**
 * Public API for User information exposed to other modules
 */
public record UserInfo(
    UUID id,
    String clerkUserId
) {
    
}
