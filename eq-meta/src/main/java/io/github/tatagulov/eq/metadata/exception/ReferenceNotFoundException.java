package io.github.tatagulov.eq.metadata.exception;

public class ReferenceNotFoundException extends ObjectNotFoundException {
    public ReferenceNotFoundException(String objectName) {
        super("Reference", objectName);
    }
}
