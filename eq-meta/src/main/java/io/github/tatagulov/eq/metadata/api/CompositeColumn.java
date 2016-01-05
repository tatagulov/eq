package io.github.tatagulov.eq.metadata.api;

import java.util.LinkedList;
import java.util.List;

public class CompositeColumn<T extends Table<T>> {

    public final T table;
    public final Column[] columns;

    public final List<CompositeColumn> childCompositeColumns = new LinkedList<CompositeColumn>();
    public CompositeColumn parentCompositeColumn;

    public CompositeColumn(T table,Column ... columns) {
        this.table = table;
        this.columns = columns;
        table.compositeColumns.add(this);
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();
        for (Column column : columns) {
            if (sb.length()>0) sb.append("_");
            sb.append(column.columnName);
        }
        return sb.toString();
    }

    public boolean contains(Column findColumn) {
        for (Column column : columns) {
            if (column == findColumn) return true;
        }
        return false;
    }
}
