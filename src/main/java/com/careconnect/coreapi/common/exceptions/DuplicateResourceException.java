package com.careconnect.coreapi.common.exceptions;

public class DuplicateResourceException extends CareConnectException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
