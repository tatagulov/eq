package io.github.tatagulov.eq.metadata.sql.api;

import io.github.tatagulov.eq.metadata.sql.Expression;
import io.github.tatagulov.eq.metadata.sql.ParamExpression;
import io.github.tatagulov.eq.metadata.sql.SQLColumn;
import io.github.tatagulov.eq.metadata.sql.SQLTable;

import java.util.List;

public interface Update<K extends SQLTable<K>> {
    <T> void set(SQLColumn<K, T> column, Expression<T> expression);

    <T> void set(SQLColumn<K, T> column, T value);

    String getSQL();

    Object[] getValues();

    List<ParamExpression> getParamExpressions();
}
