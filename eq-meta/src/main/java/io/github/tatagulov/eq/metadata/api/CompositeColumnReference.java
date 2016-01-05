package io.github.tatagulov.eq.metadata.api;

public class CompositeColumnReference<L extends Table<L>,R extends Table<R>> implements Reference<L,R> {

    public final CompositeColumn<L> primaryCompositeColumn;
    public final CompositeColumn<R> foreignCompositeColumn;

    public CompositeColumnReference(CompositeColumn<L> primaryCompositeColumn, CompositeColumn<R> foreignCompositeColumn) {
        this.primaryCompositeColumn = primaryCompositeColumn;
        this.foreignCompositeColumn = foreignCompositeColumn;
        primaryCompositeColumn.childCompositeColumns.add(foreignCompositeColumn);
        foreignCompositeColumn.parentCompositeColumn = primaryCompositeColumn;
    }
}
