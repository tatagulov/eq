package io.github.tatagulov.eq.metadata.sql;

import java.util.Collections;
import java.util.List;

public class AggregateFunctionExpression<T> extends FunctionExpression<T> {


    public AggregateFunctionExpression(String template, Class<T> type, Expression... expressions) {
        super(template, type, expressions);
    }

    public AggregateFunctionExpression(String template, Expression<T> expression) {
        super(template, expression);
    }

    @Override
    public boolean isAggregate() {
        return true;
    }

    @Override
    public List<Expression> getGroupByExpressions() {
        return Collections.emptyList();
    }
}
