package io.github.tatagulov.eq.metadata.api;

public class ColumnReference<L extends Table<L>,R extends Table<R>> implements Reference<L,R> {

    public final Column primaryColumn;
    public final Column foreignColumn;
    public final Boolean isDirect;

    public <M> ColumnReference(Column<M,L> primaryColumn,Column<M,R> foreignColumn,Boolean isDirect) {
        this.primaryColumn = primaryColumn;
        this.foreignColumn = foreignColumn;
        this.isDirect = isDirect;
        primaryColumn.childColumns.add(foreignColumn);
        foreignColumn.parentColumn = primaryColumn;

        if (isDirect) primaryColumn.directChildColumns.add(foreignColumn);
    }
}
