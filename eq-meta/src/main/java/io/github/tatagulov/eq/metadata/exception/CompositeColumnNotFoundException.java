package io.github.tatagulov.eq.metadata.exception;

public class CompositeColumnNotFoundException extends ObjectNotFoundException {

    public CompositeColumnNotFoundException(String objectName) {
        super("CompositeColumn",objectName);
    }
}
