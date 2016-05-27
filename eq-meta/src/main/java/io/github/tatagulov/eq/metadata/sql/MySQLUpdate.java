package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.sql.api.Update;

import java.util.LinkedList;
import java.util.List;

public class MySQLUpdate<K extends SQLTable<K>> implements Update<K> {

    protected final K table;

    protected final List<SQLColumn> sqlColumns = new LinkedList<SQLColumn>();
    protected final List<Expression> expressions = new LinkedList<Expression>();

    public MySQLUpdate(K table) {
        this.table = table;
    }

    @Override
    public <T> void set(SQLColumn<K, T> column, Expression<T> expression) {
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

        StringBuilder sets = new StringBuilder();
        for (SQLColumn SQLColumn : sqlColumns) {
            if (sets.length()>0) sets.append(",");
            sets.append(SQLColumn.column.columnName).append("=?");
        }
        String where = table.condition!=null ? table.condition.getSQL(aliasGenerator) :"";
        return String.format("update %s set %s%s",table.getBodySQL(aliasGenerator),sets,where);
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
