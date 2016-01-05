package io.github.tatagulov.eq.metadata.api.columns;

import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.Table;

public abstract class NumberColumn<T extends Number,K extends Table<K>> extends Column<T,K> {

    public NumberColumn(K table, String columnName, boolean isPK, Class<T> type, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, type, nullable, size, decimalDigits, isGenerated);
    }
}
