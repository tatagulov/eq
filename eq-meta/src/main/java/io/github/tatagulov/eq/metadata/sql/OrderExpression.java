package io.github.tatagulov.eq.metadata.sql;

public class OrderExpression {

    public final Expression<?> expression;
    public final OrderType orderType;

    public OrderExpression(Expression expression, OrderType orderType) {
        this.expression = expression;
        this.orderType = orderType;
    }

    public String getSQL(AliasGenerator aliasGenerator) {
        return String.format("%s %s", expression.getSQL(aliasGenerator),orderType.name());
    }

}
