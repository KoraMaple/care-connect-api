package com.careconnect.coreapi.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user doesn't have permission to access a resource.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends CareConnectException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
