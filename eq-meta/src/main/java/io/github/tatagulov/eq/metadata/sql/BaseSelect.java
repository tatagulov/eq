package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.sql.api.Select;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseSelect extends From implements Select {

    public final List<Expression> selectExpressions = new LinkedList<Expression>();
    protected final boolean distinct;
    protected final List<OrderExpression> orderExpressions = new LinkedList<OrderExpression>();

    protected Condition havingCondition;
    protected Long offset = null;
    protected Long limit = null;

    protected List<Select> unions = new LinkedList<Select>();
    protected List<Select> unionAllList = new LinkedList<Select>();

    public BaseSelect(boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public String getSQL() {
        return getSQL(new AliasGenerator("t"));
    }

    @Override
    public <T> AliasColumn<? extends Select,T> select(AliasExpression<T> aliasExpression) {
        return new AliasColumn<BaseSelect, T>(this, aliasExpression);
    }

    @Override
    public <T> void select(Expression<T> expression) {
        selectExpressions.add(expression);
    }

    @Override
    public Object[] getValues() {
        List<Object> values = new LinkedList<Object>();
        for (ParamExpression paramExpression : getParamExpressions()) {
            values.add(paramExpression.value);
        }
        return values.toArray(new Object[values.size()]);
    }

    @Override
    protected String getBodySQL(AliasGenerator aliasGenerator) {
        return "(" + getSQL(aliasGenerator) + ")";
    }

    @Override
    public void having(Condition condition) {
        if (this.havingCondition==null) {
            condition.conditionType = ConditionType.having;
            this.havingCondition = condition;
        } else {
            condition.conditionType = ConditionType.and;
            havingCondition.and(condition);
        }
    }

    @Override
    public void orderBy(final Expression expression, final OrderType orderType) {
        OrderExpression orderExpression = new OrderExpression(expression,orderType);
        orderExpressions.add(orderExpression);
    }

    @Override
    public void orderBy(final Expression expression) {
        orderBy(expression, OrderType.asc);
    }

    @Override
    public void limit(Long limit) {
        this.limit = limit;
    }

    @Override
    public void offset(Long offset) {
        this.offset = offset;
    }

    @Override
    public void union(Select select) {
        unions.add(select);
    }

    @Override
    public void unionAll(Select select) {
        unionAllList.add(select);
    }

    @Override
    public Expression asExpression() {
        return new Expression() {
            @Override
            public Class getType() {
                return BaseSelect.this.selectExpressions.get(0).getType();
            }

            @Override
            public List<From> getFrom() {
                return Collections.emptyList();
            }

            @Override
            public List<ParamExpression> getParamExpressions() {
                return BaseSelect.this.getParamExpressions();
            }

            @Override
            public boolean isAggregate() {
                return false;
            }

            @Override
            public List<Expression> getGroupByExpressions() {
                return Collections.emptyList();
            }

            @Override
            public String getSQL(AliasGenerator aliasGenerator) {
                return "(" + BaseSelect.this.getSQL(aliasGenerator) +")";
            }
        };
    }

    @Override
    public Expression asAliasExpression(String alias) {
        return new SimpleAliasExpression(asExpression(),alias);
    }
}
