package com.careconnect.coreapi.user.internal;

import java.util.Map;

import com.careconnect.coreapi.common.response.VerifiedJwtResponse;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/clerk_jwt")
    public @ResponseBody VerifiedJwtResponse clerk_jwt(@AuthenticationPrincipal String userId) {
        logger.info("User ID: {}", userId);
        return new VerifiedJwtResponse(userId);
    }

    @GetMapping("/gated_data")
    public @ResponseBody Map<String, String> get_gated_data() {
        return Map.of("foo", "bar");
    }

}