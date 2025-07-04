# HATEOAS-Compliant PageResponse Implementation

This document explains the improved `PageResponse` class that provides proper HATEOAS-compliant pagination with metadata under "meta" and content under "data".

## Response Structure

The `PageResponse` class returns data in the following JSON structure:

```json
{
  "data": [
    // Array of actual content items
  ],
  "meta": {
    "page_number": 0,
    "page_size": 20,
    "total_pages": 5,
    "total_elements": 100,
    "number_of_elements": 20,
    "has_next": true,
    "has_previous": false,
    "is_first": true,
    "is_last": false,
    "is_empty": false,
    "sort": "lastName,asc;firstName,asc"
  },
  "links": {
    "self": "http://localhost:8080/api/children?page=0&size=20",
    "first": "http://localhost:8080/api/children?page=0&size=20",
    "last": "http://localhost:8080/api/children?page=4&size=20",
    "next": "http://localhost:8080/api/children?page=1&size=20",
    "previous": null
  }
}
```

## Key Features

### 1. HATEOAS Compliance
- **`data`**: Contains the actual paginated content
- **`meta`**: Contains pagination metadata
- **`links`**: Contains navigation links for pagination

### 2. Automatic URL Generation
- Automatically detects current request context
- Builds proper absolute URLs for pagination links
- Preserves query parameters (filters, search terms, etc.)

### 3. Rich Metadata
- Complete pagination information
- Sort information included
- Boolean flags for navigation state

### 4. Flexible Usage
- Multiple constructor options
- Static factory methods for cleaner API
- Fallback to relative URLs when context unavailable

## Usage Examples

### Basic Usage in Controller
```java
@GetMapping
public PageResponse<Child> getAllChildren(@PageableDefault(size = 20) Pageable pageable) {
    Page<Child> children = childService.getAllChildren(pageable);
    return PageResponse.of(children);
}
```

### Usage in Service Layer
```java
@Service
public class ChildService {
    public PageResponse<Child> getAllChildren(Pageable pageable) {
        Page<Child> childrenPage = childRepository.findAll(pageable);
        return PageResponse.of(childrenPage);
    }
}
```

### With Custom Base URL
```java
@GetMapping
public PageResponse<Child> getAllChildren(Pageable pageable) {
    Page<Child> children = childService.getAllChildren(pageable);
    return PageResponse.of(children, "https://api.example.com/api/children");
}
```

### Using Utility Class
```java
@GetMapping
public PageResponse<Child> getAllChildren(Pageable pageable) {
    Page<Child> children = childService.getAllChildren(pageable);
    return PageResponseUtils.create(children);
}
```

## Link Generation

### Automatic Request Context Detection
The system automatically detects:
- Current request URL
- Query parameters
- Maintains filtering/search parameters in pagination links

### Query Parameter Preservation
When building pagination links, the system preserves:
- Search parameters (`?name=John`)
- Filter parameters (`?gender=male`)
- Sort parameters (handled by Spring Data)
- Custom query parameters

Example: If you call `/api/children?name=John&gender=male&page=0&size=10`, the `next` link will be:
`/api/children?name=John&gender=male&page=1&size=10`

## Metadata Fields

| Field | Description |
|-------|-------------|
| `page_number` | Current page number (0-based) |
| `page_size` | Number of items per page |
| `total_pages` | Total number of pages |
| `total_elements` | Total number of items across all pages |
| `number_of_elements` | Number of items on current page |
| `has_next` | Whether there is a next page |
| `has_previous` | Whether there is a previous page |
| `is_first` | Whether this is the first page |
| `is_last` | Whether this is the last page |
| `is_empty` | Whether the page is empty |
| `sort` | Current sort specification (if any) |

## Links

| Link | Description |
|------|-------------|
| `self` | Current page URL |
| `first` | First page URL |
| `last` | Last page URL |
| `next` | Next page URL (null if on last page) |
| `previous` | Previous page URL (null if on first page) |

## Best Practices

### 1. Use Pageable in Controllers
```java
// Good
@GetMapping
public PageResponse<Child> getAllChildren(@PageableDefault(size = 20) Pageable pageable) {
    return childService.getAllChildren(pageable);
}

// Avoid
@GetMapping
public PageResponse<Child> getAllChildren(@RequestParam int page, @RequestParam int size) {
    return childService.getAllChildren(page, size);
}
```

### 2. Consistent Page Sizes
Use `@PageableDefault` to set sensible defaults:
```java
@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
```

### 3. Let Spring Handle Pagination Parameters
Spring automatically handles `page`, `size`, and `sort` query parameters when using `Pageable`.

### 4. Preserve Business Logic in Service Layer
Keep pagination logic in the service layer and return `Page<T>` objects that can be wrapped in `PageResponse`.

## Migration Guide

### Before (Old ApiResponse with Page)
```java
@GetMapping
public ResponseEntity<ApiResponse<Page<Child>>> getAllChildren(Pageable pageable) {
    Page<Child> children = childService.getAllChildren(pageable);
    return ResponseEntity.ok(ApiResponse.success(children, "Success"));
}
```

### After (New PageResponse)
```java
@GetMapping
public PageResponse<Child> getAllChildren(@PageableDefault(size = 20) Pageable pageable) {
    Page<Child> children = childService.getAllChildren(pageable);
    return PageResponse.of(children);
}
```

## Client Usage

### Frontend/JavaScript Example
```javascript
fetch('/api/children?page=0&size=20')
  .then(response => response.json())
  .then(pageResponse => {
    // Access data
    const children = pageResponse.data;
    
    // Access metadata
    const meta = pageResponse.meta;
    console.log(`Page ${meta.page_number + 1} of ${meta.total_pages}`);
    console.log(`Showing ${meta.number_of_elements} of ${meta.total_elements} items`);
    
    // Navigate using links
    if (pageResponse.links.next) {
      fetch(pageResponse.links.next); // Get next page
    }
  });
```

This implementation provides a clean, standards-compliant approach to pagination that makes it easy for both backend developers and frontend consumers to work with paginated data.
