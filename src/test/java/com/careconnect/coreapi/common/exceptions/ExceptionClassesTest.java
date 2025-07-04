package com.careconnect.coreapi.common.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionClassesTest {

    @Test
    void resourceNotFoundException_ShouldCreateExceptionWithMessage() {
        // Given
        String message = "Resource not found";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Then
        assertThat(exception).isInstanceOf(CareConnectException.class);
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void validationException_ShouldCreateExceptionWithMessage() {
        // Given
        String message = "Validation failed";

        // When
        ValidationException exception = new ValidationException(message);

        // Then
        assertThat(exception).isInstanceOf(CareConnectException.class);
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void duplicateResourceException_ShouldCreateExceptionWithMessage() {
        // Given
        String message = "Duplicate resource";

        // When
        DuplicateResourceException exception = new DuplicateResourceException(message);

        // Then
        assertThat(exception).isInstanceOf(CareConnectException.class);
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void badRequestException_ShouldCreateExceptionWithMessage() {
        // Given
        String message = "Bad request";

        // When
        BadRequestException exception = new BadRequestException(message);

        // Then
        assertThat(exception).isInstanceOf(CareConnectException.class);
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void unauthorizedException_ShouldCreateExceptionWithMessage() {
        // Given
        String message = "Unauthorized access";

        // When
        UnauthorizedException exception = new UnauthorizedException(message);

        // Then
        assertThat(exception).isInstanceOf(CareConnectException.class);
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void forbiddenException_ShouldCreateExceptionWithMessage() {
        // Given
        String message = "Forbidden access";

        // When
        ForbiddenException exception = new ForbiddenException(message);

        // Then
        assertThat(exception).isInstanceOf(CareConnectException.class);
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void authenticationException_ShouldCreateExceptionWithMessage() {
        // Given
        String message = "Authentication failed";

        // When
        AuthenticationException exception = new AuthenticationException(message);

        // Then
        assertThat(exception).isInstanceOf(CareConnectException.class);
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void careConnectException_ShouldCreateExceptionWithMessage() {
        // Given
        String message = "Care connect error";

        // When
        CareConnectException exception = new CareConnectException(message);

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void careConnectException_ShouldCreateExceptionWithMessageAndCause() {
        // Given
        String message = "Care connect error";
        Throwable cause = new RuntimeException("Root cause");

        // When
        CareConnectException exception = new CareConnectException(message, cause);

        // Then
        assertThat(exception).isInstanceOf(RuntimeException.class);
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}
