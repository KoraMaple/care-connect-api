package com.careconnect.coreapi.common.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void constructor_ShouldSetTimestamp() {
        // When
        ApiResponse<String> response = new ApiResponse<>();

        // Then
        assertThat(response.getTimestamp()).isNotNull();
        assertThat(response.isSuccess()).isFalse(); // Default value
    }

    @Test
    void success_WithDataAndMessage_ShouldCreateSuccessResponse() {
        // Given
        String data = "test data";
        String message = "Operation successful";

        // When
        ApiResponse<String> response = ApiResponse.success(data, message);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getError()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void error_WithErrorMessage_ShouldCreateErrorResponse() {
        // Given
        String errorMessage = "Something went wrong";

        // When
        ApiResponse<String> response = ApiResponse.error(errorMessage);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getError()).isEqualTo(errorMessage);
        assertThat(response.getData()).isNull();
        assertThat(response.getMessage()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void error_WithErrorAndMessage_ShouldCreateErrorResponse() {
        // Given
        String errorMessage = "Something went wrong";
        String message = "Detailed message";

        // When
        ApiResponse<String> response = ApiResponse.error(errorMessage, message);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getError()).isEqualTo(errorMessage);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        ApiResponse<String> response = new ApiResponse<>();
        String data = "test data";
        String message = "test message";
        String error = "test error";

        // When
        response.setSuccess(true);
        response.setData(data);
        response.setMessage(message);
        response.setError(error);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getError()).isEqualTo(error);
    }
}
