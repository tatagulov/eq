package io.github.tatagulov.eq.metadata.sql.api;

import io.github.tatagulov.eq.metadata.sql.*;
import io.github.tatagulov.eq.metadata.sql.ParamExpression;

import java.util.List;

public interface Select {

    String getSQL();

    <T> AliasColumn<? extends Select,T> select(AliasExpression<T> aliasExpression);

    <T> void select(Expression<T> expression);

    Object[] getValues();

    void having(Condition condition);

    void orderBy(Expression expression, OrderType orderType);

    void orderBy(Expression expression);

    void limit(Long limit);

    void offset(Long offset);

    void union(Select select);

    void unionAll(Select select);

    Expression asExpression();

    Expression asAliasExpression(String alias);

    String getSQL(AliasGenerator aliasGenerator);

    List<ParamExpression> getParamExpressions();
}
