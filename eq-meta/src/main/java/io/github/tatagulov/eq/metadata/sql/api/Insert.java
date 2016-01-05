package io.github.tatagulov.eq.metadata.sql.api;

import io.github.tatagulov.eq.metadata.sql.Expression;
import io.github.tatagulov.eq.metadata.sql.SQLColumn;
import io.github.tatagulov.eq.metadata.sql.SQLTable;

public interface Insert<K extends SQLTable<K>>  extends Select {
    <T> void set(SQLColumn<K, T> column, Expression<T> expression);
    <T> void set(SQLColumn<K, T> column, T value);
}
