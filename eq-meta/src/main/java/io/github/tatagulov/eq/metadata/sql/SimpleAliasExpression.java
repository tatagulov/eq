package io.github.tatagulov.eq.metadata.sql;

import java.util.List;

public class SimpleAliasExpression<T> implements AliasExpression<T> {

    private final Expression<T> expression;
    private final String alias;

    public SimpleAliasExpression(Expression<T> expression, String alias) {
        this.expression = expression;
        this.alias = alias;
    }

    @Override
    public String getSQL(AliasGenerator aliasGenerator) {
        return expression.getSQL(aliasGenerator);
    }

    @Override
    public String getAlias() {
        return alias;
    }


    @Override
    public Class<T> getType() {
        return expression.getType();
    }

    @Override
    public List<ParamExpression> getParamExpressions() {
        return expression.getParamExpressions();
    }

    @Override
    public List<From> getFrom() {
        return expression.getFrom();
    }

    @Override
    public boolean isAggregate() {
        return expression.isAggregate();
    }

    @Override
    public List<Expression> getGroupByExpressions() {
        return expression.getGroupByExpressions();
    }
}
