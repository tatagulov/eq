package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.exception.ColumnNotFoundException;

import java.util.LinkedList;
import java.util.List;

public class SQLTable<K extends SQLTable<K>> extends From {

    public final Table table;
    public final List<SQLColumn<K,?>> sqlColumns = new LinkedList<SQLColumn<K,?>>();

    public SQLTable(Table table) {
        this.table = table;
    }

    public SQLColumn<K,?> getSQLColumn(String name) throws ColumnNotFoundException {
        for (SQLColumn<K, ?> sqlColumn : sqlColumns) {
            if (sqlColumn.column.columnName.equals(name)) return sqlColumn;
        }
        throw new ColumnNotFoundException(name);
    }

    @Override
    protected String getBodySQL(AliasGenerator aliasGenerator) {
        return table.getFullTableName();
    }
}
