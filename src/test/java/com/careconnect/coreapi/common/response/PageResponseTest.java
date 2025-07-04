package com.careconnect.coreapi.common.response;

import com.careconnect.coreapi.childmgmt.domain.Child;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for PageResponse to verify HATEOAS-compliant pagination
 */
class PageResponseTest {

    @Test
    void shouldCreatePageResponseWithCorrectDataStructure() {
        // Given
        List<Child> children = Arrays.asList(
            createTestChild("John", "Doe"),
            createTestChild("Jane", "Smith")
        );
        Pageable pageable = PageRequest.of(0, 20);
        Page<Child> page = new PageImpl<>(children, pageable, 50);

        // When
        PageResponse<Child> response = PageResponse.of(page);

        // Then
        assertNotNull(response);
        assertNotNull(response.getData());
        assertNotNull(response.getMeta());
        assertNotNull(response.getLinks());
        
        // Verify data content
        assertEquals(2, response.getData().size());
        assertEquals("John", response.getData().get(0).getFirstName());
        assertEquals("Jane", response.getData().get(1).getFirstName());
    }

    @Test
    void shouldCreateCorrectMetadata() {
        // Given
        List<Child> children = Arrays.asList(createTestChild("John", "Doe"));
        Pageable pageable = PageRequest.of(0, 20);
        Page<Child> page = new PageImpl<>(children, pageable, 50);

        // When
        PageResponse<Child> response = PageResponse.of(page);

        // Then
        PageResponse.PageMetadata meta = response.getMeta();
        assertEquals(0, meta.getPageNumber());
        assertEquals(20, meta.getPageSize());
        assertEquals(3, meta.getTotalPages()); // 50 items / 20 per page = 3 pages
        assertEquals(50, meta.getTotalElements());
        assertEquals(1, meta.getNumberOfElements());
        assertTrue(meta.isHasNext());
        assertFalse(meta.isHasPrevious());
        assertTrue(meta.isFirst());
        assertFalse(meta.isLast());
        assertFalse(meta.isEmpty());
    }

    @Test
    void shouldCreateCorrectLinksWithoutBaseUrl() {
        // Given
        List<Child> children = Arrays.asList(createTestChild("John", "Doe"));
        Pageable pageable = PageRequest.of(0, 20);
        Page<Child> page = new PageImpl<>(children, pageable, 50);

        // When
        PageResponse<Child> response = PageResponse.of(page);

        // Then
        PageResponse.PageLinks links = response.getLinks();
        assertNotNull(links.getSelf());
        assertNotNull(links.getFirst());
        assertNotNull(links.getLast());
        assertNotNull(links.getNext());
        assertNull(links.getPrevious()); // No previous page for first page
        
        // Verify link format (relative URLs)
        assertEquals("?page=0&size=20", links.getSelf());
        assertEquals("?page=0&size=20", links.getFirst());
        assertEquals("?page=2&size=20", links.getLast());
        assertEquals("?page=1&size=20", links.getNext());
    }

    @Test
    void shouldCreatePageResponseWithCustomBaseUrl() {
        // Given
        List<Child> children = Arrays.asList(createTestChild("John", "Doe"));
        Pageable pageable = PageRequest.of(1, 10);
        Page<Child> page = new PageImpl<>(children, pageable, 25);
        String baseUrl = "http://localhost:8080/api/children";

        // When
        PageResponse<Child> response = PageResponse.of(page, baseUrl);

        // Then
        PageResponse.PageLinks links = response.getLinks();
        assertEquals("http://localhost:8080/api/children?page=1&size=10", links.getSelf());
        assertEquals("http://localhost:8080/api/children?page=0&size=10", links.getFirst());
        assertEquals("http://localhost:8080/api/children?page=2&size=10", links.getLast());
        assertEquals("http://localhost:8080/api/children?page=2&size=10", links.getNext());
        assertEquals("http://localhost:8080/api/children?page=0&size=10", links.getPrevious());
    }

    @Test
    void shouldHandleEmptyPage() {
        // Given
        List<Child> children = List.of();
        Pageable pageable = PageRequest.of(0, 20);
        Page<Child> page = new PageImpl<>(children, pageable, 0);

        // When
        PageResponse<Child> response = PageResponse.of(page);

        // Then
        assertEquals(0, response.getData().size());
        assertTrue(response.getMeta().isEmpty());
        assertEquals(0, response.getMeta().getTotalElements());
        assertEquals(0, response.getMeta().getTotalPages()); // Empty page has 0 total pages
        assertFalse(response.getMeta().isHasNext());
        assertFalse(response.getMeta().isHasPrevious());
    }

    @Test
    void shouldHandleLastPage() {
        // Given
        List<Child> children = Arrays.asList(createTestChild("John", "Doe"));
        Pageable pageable = PageRequest.of(2, 10); // Last page
        Page<Child> page = new PageImpl<>(children, pageable, 21); // 21 items total

        // When
        PageResponse<Child> response = PageResponse.of(page);

        // Then
        PageResponse.PageMetadata meta = response.getMeta();
        assertFalse(meta.isHasNext());
        assertTrue(meta.isHasPrevious());
        assertFalse(meta.isFirst());
        assertTrue(meta.isLast());
        
        PageResponse.PageLinks links = response.getLinks();
        assertNull(links.getNext()); // No next page
        assertNotNull(links.getPrevious());
    }

    private Child createTestChild(String firstName, String lastName) {
        Child child = new Child();
        child.setId(UUID.randomUUID());
        child.setFirstName(firstName);
        child.setLastName(lastName);
        child.setDob(Instant.now().minusSeconds(365 * 24 * 60 * 60 * 5)); // 5 years ago
        child.setGender("Male");
        child.setCreatedAt(Instant.now());
        child.setUpdatedAt(Instant.now());
        return child;
    }
}
