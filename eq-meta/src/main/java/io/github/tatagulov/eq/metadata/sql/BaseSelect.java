package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.sql.api.Select;

import java.util.*;

public class BaseSelect extends From implements Select {

    protected final List<Expression> selectExpressions = new LinkedList<Expression>();
    protected final boolean distinct;
    protected final List<OrderExpression> orderExpressions = new LinkedList<OrderExpression>();

    protected Condition havingCondition;
    protected Long offset = null;
    protected Long limit = null;

    protected List<BaseSelect> unions = new LinkedList<BaseSelect>();
    protected List<BaseSelect> unionAllList = new LinkedList<BaseSelect>();

    protected From from;
    protected boolean isAggregateSelectColumn = false;

    public BaseSelect(boolean distinct) {
        this.distinct = distinct;
    }

    public BaseSelect() {
        this.distinct = false;
    }

    @Override
    public String getSQL() {
        return getSQL(new AliasGenerator("t"));
    }

    @Override
    public String getCountSQL() {
        return getCountSQL(new AliasGenerator("t"));
    }

    @Override
    public <T> AliasColumn<? extends BaseSelect,T> select(AliasExpression<T> aliasExpression) {
        return new AliasColumn<BaseSelect, T>(this, aliasExpression);
    }

    @Override
    public <T> void select(Expression<T> expression) {
        selectExpressions.add(expression);
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
    public void union(BaseSelect baseSelect) {
        unions.add(baseSelect);
    }

    @Override
    public void unionAll(BaseSelect baseSelect) {
        unionAllList.add(baseSelect);
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

    protected String getSQL(AliasGenerator aliasGenerator) {
        StringBuilder selectBuilder = new StringBuilder();
        StringBuilder groupByBuilder = new StringBuilder();
        StringBuilder orderByBuilder = new StringBuilder();

        from = getFrom();

        if (from!=null) from.generateAlias(aliasGenerator);

        for (Expression<?> expression: selectExpressions) {
            if (selectBuilder.length()>0) selectBuilder.append(",");
            selectBuilder.append(expression.getSQL(aliasGenerator));
            if (expression instanceof SimpleAliasExpression) {
                SimpleAliasExpression simpleAliasExpression = (SimpleAliasExpression) expression;
                selectBuilder.append(" as ").append(simpleAliasExpression.getAlias());
            }
        }

        for (String sql : getGroupByExpressionSQLs(aliasGenerator)) {
            if (groupByBuilder.length()>0) groupByBuilder.append(",");
            groupByBuilder.append(sql);
        }

        for (OrderExpression orderExpression : orderExpressions) {
            if (orderByBuilder.length()>0) orderByBuilder.append(",");
            orderByBuilder.append(orderExpression.getSQL(aliasGenerator));
        }
        if (groupByBuilder.length()>0) groupByBuilder.insert(0," group by ");

        String template = from !=null ? "select %s" : "values(%s)";
        String selectSQL = String.format(template, (distinct ? "distinct " : "") + selectBuilder.toString());
        String fromSQL = from!=null ? from.getFromSQL(aliasGenerator) : "";
        String groupBySQL = isAggregateSelectColumn || havingCondition!=null? groupByBuilder.toString() : "" ;
        String havingSQL = havingCondition !=null ? havingCondition.getSQL(aliasGenerator) : "";
        String orderBy = orderByBuilder.length()>0 ? " order by " + orderByBuilder : "";
        String limitSQL = limit!=null ? " limit " + limit : "";
        String offsetSQL = offset!=null ? " offset " + offset : "";

        StringBuilder sql = new StringBuilder(selectSQL + fromSQL + groupBySQL + havingSQL + orderBy + limitSQL + offsetSQL);
        for (BaseSelect select : unions) {
            sql.append(" union ").append(select.getSQL(aliasGenerator));
        }
        for (BaseSelect select : unionAllList) {
            sql.append(" union all ").append(select.getSQL(aliasGenerator));
        }
        return sql.toString();
    }

    @Override
    public Object[] getValues() {
        List<Object> values = new LinkedList<Object>();
        for (ParamExpression paramExpression : getParamExpressions()) {
            values.add(paramExpression.value);
        }
        return values.toArray(new Object[values.size()]);
    }

    protected String getCountSQL(AliasGenerator aliasGenerator) {
        StringBuilder selectBuilder = new StringBuilder();
        StringBuilder groupByBuilder = new StringBuilder();

        from = getFrom();

        if (from!=null) from.generateAlias(aliasGenerator);

        for (Expression<?> expression: selectExpressions) {
            if (selectBuilder.length()>0) selectBuilder.append(",");
            selectBuilder.append(expression.getSQL(aliasGenerator));
            if (expression instanceof SimpleAliasExpression) {
                SimpleAliasExpression simpleAliasExpression = (SimpleAliasExpression) expression;
                selectBuilder.append(" as ").append(simpleAliasExpression.getAlias());
            }
        }

        for (String sql : getGroupByExpressionSQLs(aliasGenerator)) {
            if (groupByBuilder.length()>0) groupByBuilder.append(",");
            groupByBuilder.append(sql);
        }
        if (groupByBuilder.length()>0) groupByBuilder.insert(0, " group by ");

        String selectSQL = String.format("select %s", (distinct ? "distinct " : "") + selectBuilder.toString());
        String fromSQL = from!=null ? from.getFromSQL(aliasGenerator) : "";
        String groupBySQL = isAggregateSelectColumn || havingCondition!=null? groupByBuilder.toString() : "" ;
        String havingSQL = havingCondition !=null ? havingCondition.getSQL(aliasGenerator) : "";

        StringBuilder sql;
        if (isAggregateSelectColumn || havingCondition!=null) {
            sql = new StringBuilder(String.format("select count(*) as cnt from (%s)",selectSQL + fromSQL + groupBySQL + havingSQL));
        } else {
            sql = new StringBuilder("select count(*) as cnt" + fromSQL + groupBySQL + havingSQL );
        }

        for (BaseSelect select : unions) {
            sql.append(" union ").append(select.getCountSQL(aliasGenerator));
        }
        for (BaseSelect select : unionAllList) {
            sql.append(" union all ").append(select.getCountSQL(aliasGenerator));
        }

        return (unions.size()>0 || unionAllList.size() >0) ? String.format("select sum(cnt) from (%s) as foo",sql) : sql.toString();
    }

    @Override
    public Object[] getCountValues() {
        List<Object> values = new LinkedList<Object>();
        for (ParamExpression paramExpression : getCountParamExpressions()) {
            values.add(paramExpression.value);
        }
        return values.toArray(new Object[values.size()]);
    }

    public List<ParamExpression> getParamExpressions() {
        List<ParamExpression> paramExpressions = new LinkedList<ParamExpression>();
        for (Expression<?> expression : selectExpressions) {
            paramExpressions.addAll(expression.getParamExpressions());
        }
        if (from != null) paramExpressions.addAll(from.getParamExpressions());
        if (havingCondition != null) paramExpressions.addAll(havingCondition.getParamExpressions());
        for (BaseSelect baseSelect : unions) {
            paramExpressions.addAll(baseSelect.getParamExpressions());
        }
        for (BaseSelect baseSelect : unionAllList) {
            paramExpressions.addAll(baseSelect.getParamExpressions());
        }
        return paramExpressions;
    }

    protected List<ParamExpression> getCountParamExpressions() {
        List<ParamExpression> paramExpressions = new LinkedList<ParamExpression>();
        if (from != null) paramExpressions.addAll(from.getParamExpressions());
        if (havingCondition != null) paramExpressions.addAll(havingCondition.getParamExpressions());
        for (BaseSelect baseSelect : unions) {
            paramExpressions.addAll(baseSelect.getParamExpressions());
        }
        for (BaseSelect baseSelect : unionAllList) {
            paramExpressions.addAll(baseSelect.getParamExpressions());
        }
        return paramExpressions;
    }

    protected From getFrom() {
        From rootFrom = null;
        for (Expression<?> expression : selectExpressions) {
            for (From from : expression.getFrom()) {
                From currentRootFrom = from.getRootFrom();
                if (rootFrom==null) rootFrom = currentRootFrom;
                if (currentRootFrom!=rootFrom) {
                    throw new RuntimeException("section from incorrect");
                }
            }
        }
        for (OrderExpression orderExpression : orderExpressions) {
            for (From from :  orderExpression.expression.getFrom()) {
                From currentRootFrom = from.getRootFrom();
                if (rootFrom==null) rootFrom = currentRootFrom;
                if (currentRootFrom!=rootFrom) {
                    throw new RuntimeException("section from incorrect");
                }
            }
        }

        return rootFrom;
    }


    protected Set<String> getGroupByExpressionSQLs(AliasGenerator aliasGenerator) {
        Set<String> expressions = new LinkedHashSet<String>();
        for (Expression expression : this.selectExpressions) {
            fillGroupBy(expressions, expression, aliasGenerator);
        }
        for (OrderExpression orderExpression : orderExpressions) {
            fillGroupBy(expressions, orderExpression.expression, aliasGenerator);
        }
        return expressions;
    }

    private void fillGroupBy(Set<String> expressions, Expression<?> expression, AliasGenerator aliasGenerator) {
        if (expression.isAggregate()) {
            isAggregateSelectColumn = true;
        }
        for (Expression groupByExpression : expression.getGroupByExpressions()) {
            expressions.add(groupByExpression.getSQL(aliasGenerator));
        }
    }
}
