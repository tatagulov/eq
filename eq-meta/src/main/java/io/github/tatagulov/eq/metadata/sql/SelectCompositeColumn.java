package io.github.tatagulov.eq.metadata.sql;

public class SelectCompositeColumn<T extends From,K>  {

    public final T selectTable;
    public final SQLColumn[] sqlColumns;

    public SelectCompositeColumn(T selectTable,SQLColumn... sqlColumns) {
        this.selectTable = selectTable;
        this.sqlColumns = sqlColumns;
    }

}
