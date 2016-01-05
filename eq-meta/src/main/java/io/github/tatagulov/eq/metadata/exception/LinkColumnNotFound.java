package io.github.tatagulov.eq.metadata.exception;

public class LinkColumnNotFound extends ObjectNotFoundException {
    public LinkColumnNotFound(String tableName) {
        super("link column for table ",tableName);
    }
}
