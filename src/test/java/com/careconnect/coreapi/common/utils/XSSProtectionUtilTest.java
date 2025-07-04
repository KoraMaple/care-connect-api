package com.careconnect.coreapi.common.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for XSSProtectionUtil to verify XSS protection functionality.
 */
public class XSSProtectionUtilTest {

    @Test
    public void testSanitizeText_RemovesHtmlTags() {
        // Test basic HTML removal
        String maliciousInput = "<script>alert('XSS')</script>Hello World";
        String sanitized = XSSProtectionUtil.sanitizeText(maliciousInput);
        
        assertThat(sanitized).isEqualTo("Hello World");
        assertThat(sanitized).doesNotContain("<script>");
        assertThat(sanitized).doesNotContain("alert");
    }

    @Test
    public void testSanitizeText_HandlesComplexHtml() {
        String input = "<div onclick='malicious()'>John <b>Doe</b></div>";
        String sanitized = XSSProtectionUtil.sanitizeText(input);
        
        assertThat(sanitized).isEqualTo("John Doe");
        assertThat(sanitized).doesNotContain("<div>");
        assertThat(sanitized).doesNotContain("<b>");
        assertThat(sanitized).doesNotContain("onclick");
    }

    @Test
    public void testSanitizeText_HandlesNullAndEmpty() {
        assertThat(XSSProtectionUtil.sanitizeText(null)).isNull();
        assertThat(XSSProtectionUtil.sanitizeText("")).isEmpty();
        assertThat(XSSProtectionUtil.sanitizeText("   ")).isEqualTo("   ");
    }

    @Test
    public void testSanitizeText_PreservesNormalText() {
        String normalText = "John Doe Jr.";
        String sanitized = XSSProtectionUtil.sanitizeText(normalText);
        
        assertThat(sanitized).isEqualTo(normalText);
    }

    @Test
    public void testSanitizeFormattedText_AllowsBasicFormatting() {
        String input = "Hello <b>World</b> and <i>everyone</i>!";
        String sanitized = XSSProtectionUtil.sanitizeFormattedText(input);
        
        // Should preserve basic formatting tags
        assertThat(sanitized).contains("<b>World</b>");
        assertThat(sanitized).contains("<i>everyone</i>");
    }

    @Test
    public void testSanitizeFormattedText_RemovesDangerousTags() {
        String maliciousInput = "Hello <script>alert('XSS')</script><b>World</b>";
        String sanitized = XSSProtectionUtil.sanitizeFormattedText(maliciousInput);
        
        // Should remove script but keep bold
        assertThat(sanitized).doesNotContain("<script>");
        assertThat(sanitized).doesNotContain("alert");
        assertThat(sanitized).contains("<b>World</b>");
    }

    @Test
    public void testSanitizeRichText_AllowsMoreFormatting() {
        String input = "Hello <b>World</b> and <p>paragraph text</p>";
        String sanitized = XSSProtectionUtil.sanitizeRichText(input);
        
        // Should preserve both inline and block formatting
        assertThat(sanitized).contains("<b>World</b>");
        assertThat(sanitized).contains("<p>paragraph text</p>");
    }
}
