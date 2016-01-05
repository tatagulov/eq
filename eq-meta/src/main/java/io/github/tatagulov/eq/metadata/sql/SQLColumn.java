package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.api.Column;

public class SQLColumn<K extends SQLTable<K>,T> extends BaseColumn<K,T> {
    public final K dbTable;
    public final Column<T,?> column;

    public SQLColumn(K dbTable, Column<T, ?> column) {
        super(dbTable);
        this.dbTable = dbTable;
        this.column = column;
        dbTable.sqlColumns.add(this);
    }

    @Override
    public String getSQL(AliasGenerator aliasGenerator) {
        return from.alias==null ? column.columnName : from.alias + "." + column.columnName;
    }

    @Override
    public Class<T> getType() {
        return column.type;
    }

    public SimpleAliasExpression as(String alias) {
        return new SimpleAliasExpression<T>(this,alias);
    }

    @Override
    public String getAlias() {
        return column.columnName;
    }
}
