package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.api.Table;

import java.util.LinkedList;
import java.util.List;

public class SQLTable<K extends SQLTable<K>> extends From {

    public final Table table;
    public final List<SQLColumn<K,?>> sqlColumns = new LinkedList<SQLColumn<K,?>>();

    public SQLTable(Table table) {
        this.table = table;
    }

    @Override
    protected String getBodySQL(AliasGenerator aliasGenerator) {
        return table.getFullTableName();
    }
}
