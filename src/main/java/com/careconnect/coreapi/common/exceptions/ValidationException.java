package com.careconnect.coreapi.common.exceptions;

public class ValidationException extends CareConnectException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
