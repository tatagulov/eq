package io.github.tatagulov.eq.metadata.exception;

public class SchemaNotFoundException extends ObjectNotFoundException {
    public SchemaNotFoundException(String schemaName) {
        super("Schema",schemaName);
    }
}
