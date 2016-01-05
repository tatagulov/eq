package io.github.tatagulov.eq.metadata.api;

import io.github.tatagulov.eq.metadata.exception.ColumnNotFoundException;
import io.github.tatagulov.eq.metadata.exception.CompositeColumnNotFoundException;

import java.util.LinkedList;
import java.util.List;

public class Table<K extends Table<K>> {

    public final Schema schema;
    public final String tableName;

    public final List<Column<?,K>> columns = new LinkedList<Column<?,K>>();
    public final List<Column<?,K>> keyColumns = new LinkedList<Column<?,K>>();
    public final List<CompositeColumn> compositeColumns = new LinkedList<CompositeColumn>();

    public Table(Schema schema, String tableName) {
        this.schema = schema;
        this.tableName = tableName;
        schema.tables.add(this);
    }

    public Column getColumn(String columnName) throws ColumnNotFoundException {
        Column column = findColumn(columnName);
        if (column==null) throw new ColumnNotFoundException(columnName);
        return column;
    }

    public Column findColumn(String columnName) {
        for (Column column : columns) {
            if (column.columnName.equals(columnName)) return column;
        }
        return null;
    }

    public String getFullTableName() {
        return schema.schemaName+ "."  + tableName;
    }

    public CompositeColumn getCompositeColumn(Column[] columns) {
        for (CompositeColumn compositeColumn : compositeColumns) {
            if (compareColumns(compositeColumn.columns,columns)) return compositeColumn;
        }
        return new CompositeColumn(this, columns);
    }

    public boolean compareColumns(Column[] leftColumns,Column[] rightColumns) {
        if (leftColumns.length!= rightColumns.length) return false;
        for (int i = 0; i < leftColumns.length; i++) {
            if (leftColumns[i]!=rightColumns[i]) return false;
        }
        return true;
    }

    public CompositeColumn getCompositeColumn(String selectColumnName) throws CompositeColumnNotFoundException {
        for (CompositeColumn compositeColumn : compositeColumns) {
            if (compositeColumn.getName().equals(selectColumnName)) return compositeColumn;
        }
        throw new CompositeColumnNotFoundException(selectColumnName);
    }
}
