package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.sql.api.Update;

import java.util.LinkedList;
import java.util.List;

public class PostgresUpdate<K extends SQLTable<K>> implements Update<K> {

    protected final K table;

    protected final List<SQLColumn> sqlColumns = new LinkedList<SQLColumn>();
    protected final List<Expression> expressions = new LinkedList<Expression>();

    public PostgresUpdate(K table) {
        this.table = table;
    }

    @Override
    public <T> void set(SQLColumn<K, T> column, Expression<T> expression){
        sqlColumns.add(column);
        expressions.add(expression);
    }

    @Override
    public <T> void set(SQLColumn<K, T> column, T value) {
        set(column, new ParamExpression<T>(value, column.getType()));
    }

    @Override
    public String getSQL() {
        AliasGenerator aliasGenerator = new AliasGenerator("t");
        table.generateAlias(aliasGenerator);
        if (isSubSelect()) {
            From from = getFrom();
            from.generateAlias(aliasGenerator);

            StringBuilder fields = new StringBuilder();
            for (SQLColumn SQLColumn : sqlColumns) {
                if (fields.length()>0) fields.append(",");
                fields.append(SQLColumn.column.columnName);
            }
            StringBuilder values = new StringBuilder();
            for (Expression expression : expressions) {
                if (values.length()>0) values.append(",");
                values.append(expression.getSQL(aliasGenerator));
            }

            if (table.condition!=null) from.where(table.condition);
            String fromSQL = from.getFromSQL(aliasGenerator);


            return String.format("update %s set (%s) = (%s)%s",table.getBodySQL(aliasGenerator) + " as " + table.alias,fields,values,fromSQL);

        } else {
            StringBuilder fields = new StringBuilder();
            for (SQLColumn SQLColumn : sqlColumns) {
                if (fields.length()>0) fields.append(",");
                fields.append(SQLColumn.column.columnName);
            }
            StringBuilder values = new StringBuilder();
            for (Expression expression : expressions) {
                if (values.length()>0) values.append(",");
                values.append(expression.getSQL(aliasGenerator));
            }
            String where = table.condition!=null ? table.condition.getSQL(aliasGenerator) :"";
            return String.format("update %s set (%s) = (%s)%s",table.getBodySQL(aliasGenerator)+ " as " + table.alias,fields,values,where);
        }
    }

    private From getFrom() {
        From rootFrom = null;
        for (Expression<?> expression : expressions) {
            for (From from : expression.getFrom()) {
                if (from==table) continue;
                From currentRootFrom = from.getRootFrom();
                if (rootFrom==null) rootFrom = currentRootFrom;
                if (currentRootFrom!=rootFrom) {
                    throw new RuntimeException("section from incorrect");
                }
            }
        }
        return rootFrom;
    }

    protected boolean isSubSelect() {
        for (Expression<?> expression : expressions) {
            for (From from : expression.getFrom()) {
                if (from!=table) {
                    return true;
                }
            }
        }
        return false;
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
    public List<ParamExpression> getParamExpressions() {
        List<ParamExpression> paramExpressions = new LinkedList<ParamExpression>();
        for (Expression<?> expression : expressions) {
            paramExpressions.addAll(expression.getParamExpressions());
        }
        if (table.condition!=null) paramExpressions.addAll(table.condition.getParamExpressions());
        return paramExpressions;
    }
}
