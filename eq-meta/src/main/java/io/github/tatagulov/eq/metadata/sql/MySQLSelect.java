package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.sql.api.Select;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MySQLSelect extends BaseSelect {


    protected From from;
    protected boolean isAggregate = false;

    public MySQLSelect(boolean distinct) {
        super(distinct);
    }

    public MySQLSelect() {
        super(false);
    }

    @Override
    public String getSQL(AliasGenerator aliasGenerator) {
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
        String template = from!=null ?"select %s" : "values(%s)";

        String selectSQL = String.format(template, (distinct ? "distinct " : "") + selectBuilder.toString());
        String fromSQL = from!=null ? from.getFromSQL(aliasGenerator) : "";
        String groupBySQL = isAggregate|| havingCondition!=null? groupByBuilder.toString() : "" ;
        String havingSQL = havingCondition !=null ? havingCondition.getSQL(aliasGenerator) : "";
        String orderBy = orderByBuilder.length()>0 ? " order by " + orderByBuilder : "";
        String limitSQL = limit!=null ? " limit " + limit : "";
        String offsetSQL = offset!=null ? " offset " + offset : "";

        StringBuilder sql = new StringBuilder(selectSQL + fromSQL + groupBySQL + havingSQL + orderBy + limitSQL + offsetSQL);
        for (Select select : unions) {
            sql.append(" union ").append(select.getSQL(aliasGenerator));
        }
        for (Select select : unionAllList) {
            sql.append(" union all ").append(select.getSQL(aliasGenerator));
        }
        return sql.toString();
    }

    @Override
    public List<ParamExpression> getParamExpressions() {
        List<ParamExpression> paramExpressions = new LinkedList<ParamExpression>();
        for (Expression<?> expression : selectExpressions) {
            paramExpressions.addAll(expression.getParamExpressions());
        }
        if (from != null) paramExpressions.addAll(from.getParamExpressions());
        if (havingCondition != null) paramExpressions.addAll(havingCondition.getParamExpressions());
        for (Select baseSelect : unions) {
            paramExpressions.addAll(baseSelect.getParamExpressions());
        }
        for (Select baseSelect : unionAllList) {
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
            isAggregate = true;
        }
        for (Expression groupByExpression : expression.getGroupByExpressions()) {
            expressions.add(groupByExpression.getSQL(aliasGenerator));
        }
    }

}
