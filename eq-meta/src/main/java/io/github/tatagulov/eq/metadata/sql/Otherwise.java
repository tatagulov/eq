package io.github.tatagulov.eq.metadata.sql;

import java.util.LinkedList;
import java.util.List;

public class Otherwise<T> implements Expression<T> {

    class WhenCondition {
        Condition condition;
        Expression<T> expression;
    }

    protected final List<WhenCondition> whenConditions = new LinkedList<WhenCondition>();
    protected Expression<T> otherwiseExpression;

    @Override
    public String getSQL(AliasGenerator aliasGenerator) {
        StringBuilder stringBuilder = new StringBuilder("case");
        for (WhenCondition whenCondition : whenConditions) {
            String when = String.format("%s then %s", whenCondition.condition.getSQL(aliasGenerator), whenCondition.expression.getSQL(aliasGenerator));
            stringBuilder.append(when);
        }
        String otherwise = String.format(" else %s end", otherwiseExpression.getSQL(aliasGenerator));
        stringBuilder.append(otherwise);
        return stringBuilder.toString();
    }

    @Override
    public Class<T> getType() {
        return otherwiseExpression.getType();
    }

    @Override
    public List<From> getFrom() {
        List<From> froms = new LinkedList<From>();
        for (WhenCondition whenCondition : whenConditions) {
            froms.addAll(whenCondition.condition.getFrom());
            froms.addAll(whenCondition.expression.getFrom());
        }
        froms.addAll(otherwiseExpression.getFrom());
        return froms;
    }

    @Override
    public List<ParamExpression> getParamExpressions() {
        List<ParamExpression> paramExpressions = new LinkedList<ParamExpression>();
        for (WhenCondition whenCondition : whenConditions) {
            paramExpressions.addAll(whenCondition.expression.getParamExpressions());
            paramExpressions.addAll(whenCondition.condition.getParamExpressions());
        }
        paramExpressions.addAll(otherwiseExpression.getParamExpressions());
        return paramExpressions;
    }

    @Override
    public boolean isAggregate() {
        for (WhenCondition whenCondition : whenConditions) {
            if (whenCondition.expression.isAggregate()) return true;
            if (whenCondition.condition.isAggregate()) return true;
        }
        return otherwiseExpression.isAggregate();
    }

    @Override
    public List<Expression> getGroupByExpressions() {
        List<Expression> expressions = new LinkedList<Expression>();
        for (WhenCondition whenCondition : whenConditions) {
            expressions.addAll(whenCondition.expression.getGroupByExpressions());
            expressions.addAll(whenCondition.condition.getGroupByExpressions());
        }
        expressions.addAll(otherwiseExpression.getGroupByExpressions());
        return expressions;
    }

    public AliasExpression<T> as(String alias) {
        return new SimpleAliasExpression<T>(this,alias);
    }
}
