package io.github.tatagulov.eq.metadata.sql;

public class SQLFunction {

    public static <T> FunctionExpression<T> max(final Expression<T> expression) {
        return new AggregateFunctionExpression<T>("max(%s)", expression);
    }

    public static <T> FunctionExpression<T> min(final Expression<T> expression) {
        return new AggregateFunctionExpression<T>("min(%s)", expression);
    }

    public static FunctionExpression<Long> count(final Expression<?> expression) {
        return new AggregateFunctionExpression<Long>("count(%s)",Long.class, expression);
    }

    public static FunctionExpression<Number> sum(final Expression<? extends Number> expression) {
        return new AggregateFunctionExpression<Number>("sum(%s)",Number.class, expression);
    }

    public static FunctionExpression<Number> avg(final Expression<? extends Number> expression) {
        return new AggregateFunctionExpression<Number>("avg(%s)",Number.class, expression);
    }

    public static FunctionExpression<String> upper(final Expression<String> expression) {
        return new FunctionExpression<String>("upper(%s)", expression);
    }

    public static FunctionExpression<String> concat(final Expression... expressions) {
        return new FunctionExpression<String>("",String.class, expressions) {
            @Override
            public String getSQL(AliasGenerator aliasGenerator) {
                StringBuilder sb = new StringBuilder();
                for (Expression expression : expressions) {
                    if (sb.length()>0) sb.append(",");
                    sb.append(expression.getSQL(aliasGenerator));
                }
                return "concat("+sb.toString()+")";
            }
        };
    }

    public static  <T> When<T> when(Condition condition, Expression<T> expression) {
        When<T> when = new When<T>();
        when.when(condition, expression);
        return when;
    }

    public static <T> Expression<T> plus(Expression<T> left,Expression<T> right) {
        return new FunctionExpression<T>("%s + %s",left.getType(),left,right);
    }

    public static <T> Expression<T> minus(Expression<T> left,Expression<T> right) {
        return new FunctionExpression<T>("%s - %s",left.getType(),left,right);
    }

    public static <T> Expression<T> multi(Expression<T> left,Expression<T> right) {
        return new FunctionExpression<T>("%s * %s",left.getType(),left,right);
    }

    public static <T> Expression<T> div(Expression<T> left,Expression<T> right) {
        return new FunctionExpression<T>("%s / %s",left.getType(),left,right);
    }

    public static <T> Expression<T> mod(Expression<T> left,Expression<T> right) {
        return new FunctionExpression<T>("%s % %s",left.getType(),left,right);
    }
}
