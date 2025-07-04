package com.careconnect.coreapi.common.utils;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Component;

/**
 * Utility class for sanitizing user input to prevent XSS attacks.
 * Uses OWASP Java HTML Sanitizer for safe HTML content processing.
 */
public class XSSProtectionUtil {
    
    // Private constructor to hide implicit public one
    private XSSProtectionUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // Basic text sanitizer - removes all HTML tags
    private static final PolicyFactory TEXT_SANITIZER = Sanitizers.FORMATTING.and(Sanitizers.BLOCKS);
    
    // Strict sanitizer - only allows basic formatting (bold, italic, etc.)
    private static final PolicyFactory BASIC_FORMATTING_SANITIZER = Sanitizers.FORMATTING;
    
    // No HTML policy - strips everything
    private static final PolicyFactory NO_HTML_POLICY = new HtmlPolicyBuilder().toFactory();
    
    /**
     * Sanitizes text input by removing all HTML tags.
     * Use this for names, basic text fields where no HTML should be allowed.
     * 
     * @param input the input string to sanitize
     * @return sanitized string with all HTML removed
     */
    public static String sanitizeText(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        // Use empty policy to strip all HTML content completely
        return NO_HTML_POLICY.sanitize(input).trim();
    }
    
    /**
     * Sanitizes content allowing basic formatting (bold, italic, etc.).
     * Use this for longer text fields where some formatting might be acceptable.
     * 
     * @param input the input string to sanitize
     * @return sanitized string with safe HTML tags preserved
     */
    public static String sanitizeFormattedText(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        return BASIC_FORMATTING_SANITIZER.sanitize(input).trim();
    }
    
    /**
     * Sanitizes content allowing text formatting and basic block elements.
     * Use this for rich text fields where more formatting is needed.
     * 
     * @param input the input string to sanitize
     * @return sanitized string with safe HTML preserved
     */
    public static String sanitizeRichText(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        return TEXT_SANITIZER.sanitize(input).trim();
    }
}
