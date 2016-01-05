package io.github.tatagulov.eq.metadata.exception;

public abstract class ObjectNotFoundException extends Exception {
    public ObjectNotFoundException(String typeName, String objectName) {
        super(String.format("%s \"%s\" not found",typeName,objectName));
    }
}
