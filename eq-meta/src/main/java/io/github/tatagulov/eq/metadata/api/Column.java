package io.github.tatagulov.eq.metadata.api;

import io.github.tatagulov.eq.metadata.exception.ReferenceNotFoundException;

import java.util.LinkedList;
import java.util.List;

public abstract class Column<T,K extends Table<K>> {

    public final K table;
    public final String columnName;
    public final boolean isPK;
    public final Class<T> type;
    public final boolean nullable;
    public final int size;
    public final int decimalDigits;
    public final List<Column> childColumns = new LinkedList<Column>();
    public final List<Column> directChildColumns = new LinkedList<Column>();
    public final boolean isGenerated;
    public Column parentColumn;

    public Column(K table, String columnName, boolean isPK, Class<T> type, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        this.table = table;
        this.columnName = columnName;
        this.isPK = isPK;
        this.type = type;
        this.nullable = nullable;
        this.size = size;
        this.decimalDigits = decimalDigits;
        this.isGenerated = isGenerated;
        table.columns.add(this);
        if (isPK) table.keyColumns.add(this);
    }

    public Column getLinkColumn(String linkTableName, String linkColumnName) throws ReferenceNotFoundException {
        if (checkColumn(parentColumn,linkTableName, linkColumnName)) return parentColumn;
        for (Column childColumn : childColumns) {
            if (checkColumn(childColumn,linkTableName,linkColumnName)) return childColumn;
        }
        String message = table.getFullTableName() +"." +columnName +"->" + linkTableName + "." + linkColumnName;
        throw new ReferenceNotFoundException(message);
    }

    private boolean checkColumn(Column column, String tableName, String columnName) {
        return column != null && column.table.getFullTableName().equals(tableName) && column.columnName.equals(columnName);
    }
}
