package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.sql.api.Insert;

import java.util.LinkedList;
import java.util.List;

public class PostgresInsert<K extends SQLTable<K>> extends PostgresSelect implements Insert<K> {

    protected final K table;
    protected final List<SQLColumn> sqlColumns = new LinkedList<SQLColumn>();
    protected boolean useSubSelect;

    public PostgresInsert(K table) {
        super(false);
        this.table = table;
    }

    public PostgresInsert(boolean distinct, K table) {
        super(distinct);
        this.table = table;
    }

    @Override
    public String getSQL() {
        StringBuilder fields = new StringBuilder();
        for (SQLColumn SQLColumn : sqlColumns) {
            if (fields.length()>0) fields.append(",");
            fields.append(SQLColumn.column.columnName);
        }
        AliasGenerator aliasGenerator = new AliasGenerator("t");
        return String.format("insert into %s(%s) %s",table.table.getFullTableName(),fields,super.getSQL(aliasGenerator));
    }

    @Override
    public <T> void set(SQLColumn<K, T> column, Expression<T> expression) {
        sqlColumns.add(column);
        select(expression);
    }

    @Override
    public <T> void set(SQLColumn<K, T> column, T value) {
        set(column,new ParamExpression<T>(value,column.getType()));
    }
}
