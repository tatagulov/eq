package io.github.tatagulov.eq.metadata.exception;

public class ColumnNotFoundException extends ObjectNotFoundException {
    public ColumnNotFoundException(String objectName) {
        super("EQColumn",objectName);
    }
}
