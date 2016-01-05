package io.github.tatagulov.eq.metadata.sql;

public interface AliasExpression<T> extends Expression<T> {
    String getAlias();
}

