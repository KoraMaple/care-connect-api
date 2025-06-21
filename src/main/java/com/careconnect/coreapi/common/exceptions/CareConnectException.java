package com.careconnect.coreapi.common.exceptions;

/**
 * Base exception for all application-specific exceptions.
 */
public class CareConnectException extends RuntimeException {

    public CareConnectException(String message) {
        super(message);
    }

    public CareConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
