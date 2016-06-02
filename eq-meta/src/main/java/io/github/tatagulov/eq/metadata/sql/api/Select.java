package io.github.tatagulov.eq.metadata.sql.api;

import io.github.tatagulov.eq.metadata.sql.*;

public interface Select {

    String getSQL();

    <T> AliasColumn<? extends Select,T> select(AliasExpression<T> aliasExpression);

    <T> void select(Expression<T> expression);

    Object[] getValues();

    String getCountSQL();

    Object[] getCountValues();

    void having(Condition condition);

    void orderBy(Expression expression, OrderType orderType);

    void orderBy(Expression expression);

    void limit(Long limit);

    void offset(Long offset);

    void union(BaseSelect select);

    void unionAll(BaseSelect select);

    Expression asExpression();

    Expression asAliasExpression(String alias);
}
