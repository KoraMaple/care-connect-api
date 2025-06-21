package com.careconnect.coreapi.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when validation fails on a request payload.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends CareConnectException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
