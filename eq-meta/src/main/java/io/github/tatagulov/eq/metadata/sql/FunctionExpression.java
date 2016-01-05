package io.github.tatagulov.eq.metadata.sql;


import java.util.LinkedList;
import java.util.List;

public class FunctionExpression<T> implements Expression<T> {

    private final String template;
    private final Class<T> type;
    private final Expression[] expressions;

    public FunctionExpression(String template, Class<T> type, Expression... expressions) {
        this.template = template;
        this.type = type;
        this.expressions = expressions;
    }

    public FunctionExpression(String template, Expression<T> expression) {
        this.template = template;
        this.type = expression.getType();
        this.expressions = new Expression[]{expression};
    }

    public AliasExpression<T> as(String alias) {
        return new SimpleAliasExpression<T>(this,alias);
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public String getSQL(AliasGenerator aliasGenerator) {
        String[] strings = new String[expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            Expression expression = expressions[i];
            strings[i] = expression.getSQL(aliasGenerator);
        }
        return String.format(template,(Object[])strings);
    }

    @Override
    public List<ParamExpression> getParamExpressions() {
        List<ParamExpression> paramExpressions = new LinkedList<ParamExpression>();
        for (Expression<?> expression : expressions) {
            paramExpressions.addAll(expression.getParamExpressions());
        }
        return paramExpressions;
    }

    @Override
    public List<From> getFrom() {
        List<From> froms = new LinkedList<From>();
        for (Expression<?> expression : expressions) {
            froms.addAll(expression.getFrom());
        }
        return froms;
    }


    @Override
    public boolean isAggregate() {
        for (Expression expression : expressions) {
            if (expression.isAggregate()) return true;
        }
        return false;
    }

    @Override
    public List<Expression> getGroupByExpressions() {
        List<Expression> groupByExpressions = new LinkedList<Expression>();
        for (Expression<?> expression : expressions) {
            groupByExpressions.addAll(expression.getGroupByExpressions());
        }
        return groupByExpressions;
    }
}
