package com.careconnect.coreapi.common.filter;

import com.clerk.backend_api.helpers.security.AuthenticateRequest;
import com.clerk.backend_api.helpers.security.models.AuthenticateRequestOptions;
import com.clerk.backend_api.helpers.security.models.MachineAuthVerificationData;
import com.clerk.backend_api.helpers.security.models.RequestState;
import com.careconnect.coreapi.common.config.SecurityProperties;
import com.careconnect.coreapi.common.exceptions.AuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(RequestAuthenticationFilter.class);
    private final SecurityProperties securityProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Value("${clerk.api.secret-key}")
    private String clerkApiSecretKey;

    @Value("${clerk.api.authorized-parties}")
    private List<String> clerkApiAuthorizedParties;

    public RequestAuthenticationFilter(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        
        // Skip authentication if security is disabled or endpoint is public
        if (!securityProperties.isEnabled() || isPublicEndpoint(request)) {
            try {
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                logger.error("Error processing request", e);
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
            }
            return;
        }
        
        try {
            Map<String, List<String>> headers = new HashMap<>();
            request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
                List<String> headerValues = new ArrayList<>();
                request.getHeaders(headerName).asIterator().forEachRemaining(headerValues::add);
                headers.put(headerName, headerValues);
            });

            // authenticate with clerk API
            RequestState state = AuthenticateRequest.authenticateRequest(headers,
                    AuthenticateRequestOptions.Builder.withSecretKey(clerkApiSecretKey).authorizedParties(clerkApiAuthorizedParties).build()
            );

            if (!state.isSignedIn()){
                String reason = state.reason().map(r -> r.message()).orElse("Unknown authentication error");
                throw new AuthenticationException(reason);
            }

            // Debug: Log the state to see what's available
            if (logger.isDebugEnabled()) {
                logger.debug("Authentication state: {}", state);
                if (state.claims().isPresent()) {
                    logger.debug("Claims: {}", state.claims().get());
                }
            }

            String userId = extractUserId(state);
            if (userId == null) {
                throw new AuthenticationException("Unable to authenticate request, no user_id found in claims or token verification response");
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Authenticated user ID: {}", userId);
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            logger.error("Authentication error: {}", e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            logger.error("Authentication error", e);
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unable to authenticate request: " + e.getMessage());
        }
    }

    /**
     * Checks if the request path matches any of the configured public endpoints.
     *
     * @param request The HTTP request
     * @return true if the endpoint is public, false otherwise
     */
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        return securityProperties.getPublicEndpoints().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }

    /**
     * Extracts the user ID from the request state.
     *
     * @param state The request state from Clerk authentication
     * @return The user ID, or null if not found
     */
    private String extractUserId(RequestState state) {
        // Check for standard Clerk claim patterns
        if (state.claims().isPresent()){
            // Try multiple common claim names for user ID
            Map<String, Object> claims = state.claims().get();
            if (claims.containsKey("user_id")) {
                return (String) claims.get("user_id");
            } else if (claims.containsKey("sub")) {
                return (String) claims.get("sub");
            } else if (claims.containsKey("azp")) {
                return (String) claims.get("azp");
            }

            // Log all available claims for debugging
            if (logger.isDebugEnabled()) {
                logger.debug("Available claims: {}", claims.keySet());
            }
        }
        else if (state.tokenVerificationResponse().isPresent()){
            try {
                var tokenResponseOpt = state.tokenVerificationResponse();
                if (tokenResponseOpt.isPresent()) {
                    var tokenResponse = tokenResponseOpt.get();
                    if (tokenResponse.payload() instanceof MachineAuthVerificationData) {
                        return ((MachineAuthVerificationData) tokenResponse.payload()).getSubject();
                    }
                }
            } catch (Exception e) {
                logger.error("Error extracting subject from token verification response", e);
            }
        }
        return null;
    }

    /**
     * Sends an error response to the client.
     *
     * @param response The HTTP response
     * @param statusCode The HTTP status code
     * @param message The error message
     * @throws IOException If an I/O error occurs
     */
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.addHeader("Content-Type", "application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
