package com.careconnect.coreapi.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HATEOAS-compliant pageable response wrapper that provides structured pagination
 * with metadata under "meta" and content under "data" keys.
 * 
 * Response structure:
 * {
 *   "data": [...],
 *   "meta": {
 *     "page_number": 0,
 *     "page_size": 20,
 *     "total_pages": 5,
 *     "total_elements": 100,
 *     ...
 *   },
 *   "links": {
 *     "self": "...",
 *     "first": "...",
 *     "last": "...",
 *     "next": "...",
 *     "previous": "..."
 *   }
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    
    @JsonProperty("data")
    private List<T> data;
    
    @JsonProperty("meta")
    private PageMetadata meta;
    
    @JsonProperty("links")
    private PageLinks links;

    /**
     * Create a PageResponse from a Spring Data Page with automatic request context detection
     */
    public PageResponse(Page<T> page) {
        this.data = page.getContent();
        this.meta = buildMetadata(page);
        this.links = buildLinks(page, getCurrentRequestUri(), getCurrentRequestParameters());
    }

    /**
     * Create a PageResponse from a Spring Data Page with explicit base URL
     */
    public PageResponse(Page<T> page, String baseUrl) {
        this.data = page.getContent();
        this.meta = buildMetadata(page);
        this.links = buildLinks(page, baseUrl, getCurrentRequestParameters());
    }

    /**
     * Create a PageResponse from a Spring Data Page with explicit base URL and parameters
     */
    public PageResponse(Page<T> page, String baseUrl, Map<String, String[]> requestParams) {
        this.data = page.getContent();
        this.meta = buildMetadata(page);
        this.links = buildLinks(page, baseUrl, requestParams);
    }

    /**
     * Static factory method for cleaner API
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(page);
    }

    /**
     * Static factory method with base URL
     */
    public static <T> PageResponse<T> of(Page<T> page, String baseUrl) {
        return new PageResponse<>(page, baseUrl);
    }

    private PageMetadata buildMetadata(Page<T> page) {
        return PageMetadata.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .numberOfElements(page.getNumberOfElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .isEmpty(page.isEmpty())
                .sort(page.getSort().isSorted() ? 
                    page.getSort().stream()
                        .map(order -> order.getProperty() + "," + order.getDirection().name().toLowerCase())
                        .collect(Collectors.joining(";")) : null)
                .build();
    }

    private PageLinks buildLinks(Page<T> page, String baseUrl, Map<String, String[]> requestParams) {
        if (baseUrl == null) {
            // Fallback to simple relative links if no base URL available
            return PageLinks.builder()
                    .self(buildSimplePageLink(page.getNumber(), page.getSize()))
                    .first(buildSimplePageLink(0, page.getSize()))
                    .last(buildSimplePageLink(Math.max(0, page.getTotalPages() - 1), page.getSize()))
                    .next(page.hasNext() ? buildSimplePageLink(page.getNumber() + 1, page.getSize()) : null)
                    .previous(page.hasPrevious() ? buildSimplePageLink(page.getNumber() - 1, page.getSize()) : null)
                    .build();
        }

        return PageLinks.builder()
                .self(buildPageLink(baseUrl, page.getNumber(), page.getSize(), requestParams))
                .first(buildPageLink(baseUrl, 0, page.getSize(), requestParams))
                .last(buildPageLink(baseUrl, Math.max(0, page.getTotalPages() - 1), page.getSize(), requestParams))
                .next(page.hasNext() ? buildPageLink(baseUrl, page.getNumber() + 1, page.getSize(), requestParams) : null)
                .previous(page.hasPrevious() ? buildPageLink(baseUrl, page.getNumber() - 1, page.getSize(), requestParams) : null)
                .build();
    }

    private String getCurrentRequestUri() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getRequestURL().toString();
            }
        } catch (Exception e) {
            // Ignore and return null - will use fallback
        }
        return null;
    }

    private Map<String, String[]> getCurrentRequestParameters() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getParameterMap();
            }
        } catch (Exception e) {
            // Ignore and return empty map
        }
        return Map.of();
    }

    private String buildSimplePageLink(int pageNumber, int pageSize) {
        return String.format("?page=%d&size=%d", pageNumber, pageSize);
    }

    private String buildPageLink(String baseUrl, int pageNumber, int pageSize, Map<String, String[]> requestParams) {
        StringBuilder linkBuilder = new StringBuilder(baseUrl);
        linkBuilder.append("?page=").append(pageNumber).append("&size=").append(pageSize);
        
        // Include non-pagination parameters in the links
        if (requestParams != null) {
            requestParams.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals("page") && !entry.getKey().equals("size"))
                    .forEach(entry -> {
                        String[] values = entry.getValue();
                        if (values != null && values.length > 0 && values[0] != null && !values[0].trim().isEmpty()) {
                            linkBuilder.append("&").append(entry.getKey()).append("=").append(values[0]);
                        }
                    });
        }
        
        return linkBuilder.toString();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageMetadata {
        @JsonProperty("page_number")
        private int pageNumber;
        
        @JsonProperty("page_size")
        private int pageSize;
        
        @JsonProperty("total_pages")
        private int totalPages;
        
        @JsonProperty("total_elements")
        private long totalElements;
        
        @JsonProperty("number_of_elements")
        private int numberOfElements;
        
        @JsonProperty("has_next")
        private boolean hasNext;
        
        @JsonProperty("has_previous")
        private boolean hasPrevious;
        
        @JsonProperty("is_first")
        private boolean isFirst;
        
        @JsonProperty("is_last")
        private boolean isLast;
        
        @JsonProperty("is_empty")
        private boolean isEmpty;
        
        @JsonProperty("sort")
        private String sort;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageLinks {
        private String self;
        private String first;
        private String last;
        private String next;
        private String previous;
    }
}
