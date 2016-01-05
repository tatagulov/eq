package io.github.tatagulov.eq.metadata.api.columns.number;

import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.api.columns.NumberColumn;

public class FloatColumn<K extends Table<K>> extends NumberColumn<Float,K> {

    public FloatColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, Float.class, nullable, size, decimalDigits, isGenerated);
    }

}
