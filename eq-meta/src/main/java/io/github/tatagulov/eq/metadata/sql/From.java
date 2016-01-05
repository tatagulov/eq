package io.github.tatagulov.eq.metadata.sql;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public abstract class From {

    protected String alias;
    protected JoinType joinType;
    protected Condition condition;
    protected From parent;
    protected LinkedHashSet<From> childFroms = new LinkedHashSet<From>();


    String getFromSQL(AliasGenerator aliasGenerator) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" %s %s %s", joinType == null ? "from" : joinType.sql, getBodySQL(aliasGenerator), alias));
        for (From childTable : childFroms) {
            sb.append(childTable.getFromSQL(aliasGenerator));
        }
        if (condition!=null) sb.append(condition.getSQL(aliasGenerator));
        return sb.toString();
    }

    protected abstract String getBodySQL(AliasGenerator aliasGenerator);

    From getRootFrom() {
        if (parent!=null) {
            parent.childFroms.add(this);
            return parent.getRootFrom();
        }
        return this;
    }

    public <T extends From,L extends From,M> L join(JoinType joinType,BaseColumn<T, M> leftSelectColumn, BaseColumn<L, M> rightSelectColumn) {
        final L selectTable = rightSelectColumn.getSelectTable();
        selectTable.joinType = joinType;
        selectTable.where(new TwoCondition<M>(leftSelectColumn, Criteria.EQ, rightSelectColumn));
        selectTable.parent = this;
        return selectTable;
    }

    public Condition where(Condition newCondition) {
        if (parent!=null) parent.childFroms.add(this);
        if (condition==null) {
            newCondition.conditionType = joinType == null ? ConditionType.where : ConditionType.on;
            condition = newCondition;
        } else {
            newCondition.conditionType = ConditionType.and;
            condition.and(newCondition);
        }
        return condition;
    }


    public List<ParamExpression> getParamExpressions() {
        List<ParamExpression> paramExpressions = new LinkedList<ParamExpression>();
        for (From childFrom : childFroms) {
            paramExpressions.addAll(childFrom.getParamExpressions());
        }
        if (condition!=null) paramExpressions.addAll(condition.getParamExpressions());
        return paramExpressions;
    }


    void generateAlias(AliasGenerator aliasGenerator) {
        this.alias = aliasGenerator.generate();
        for (From childFrom : childFroms) {
            childFrom.generateAlias(aliasGenerator);
        }
    }
}
