package com.careconnect.coreapi.common.response;

import org.springframework.data.domain.Page;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility class for creating HATEOAS-compliant PageResponse objects
 * with proper URL handling and request context detection.
 */
public class PageResponseUtils {

    private PageResponseUtils() {
        // Utility class - private constructor
    }

    /**
     * Create a PageResponse with automatic request context detection.
     * This will attempt to build proper absolute URLs from the current request.
     * 
     * @param page The Spring Data Page object
     * @param <T> The type of data in the page
     * @return A properly constructed PageResponse
     */
    public static <T> PageResponse<T> create(Page<T> page) {
        return PageResponse.of(page);
    }

    /**
     * Create a PageResponse with explicit base URL.
     * Use this when you need to override the automatic URL detection.
     * 
     * @param page The Spring Data Page object
     * @param baseUrl The base URL for pagination links
     * @param <T> The type of data in the page
     * @return A properly constructed PageResponse
     */
    public static <T> PageResponse<T> create(Page<T> page, String baseUrl) {
        return PageResponse.of(page, baseUrl);
    }

    /**
     * Get the current request URI if available.
     * 
     * @return The current request URI or null if not available
     */
    public static String getCurrentRequestUri() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getRequestURL().toString();
            }
        } catch (Exception e) {
            // Ignore exception - return null
        }
        return null;
    }

    /**
     * Check if we're currently in a web request context.
     * 
     * @return true if we're in a web request context, false otherwise
     */
    public static boolean isInWebRequestContext() {
        try {
            return RequestContextHolder.getRequestAttributes() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
