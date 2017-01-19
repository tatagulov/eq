package io.github.tatagulov.eq.metadata.exception;

public class TableNotFoundException extends ObjectNotFoundException {

    public TableNotFoundException(String objectName) {
        super("EQTable",objectName);
    }
}
