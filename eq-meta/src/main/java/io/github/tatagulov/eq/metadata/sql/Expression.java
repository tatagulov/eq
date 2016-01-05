package io.github.tatagulov.eq.metadata.sql;

import java.util.List;

public interface Expression<T> {
    Class<T> getType();
    List<From> getFrom();
    List<ParamExpression> getParamExpressions();
    boolean isAggregate();
    List<Expression> getGroupByExpressions();
    String getSQL(AliasGenerator aliasGenerator);
}