package io.github.tatagulov.eq.metadata.api.columns.number;

import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.api.columns.NumberColumn;

public class DoubleColumn<K extends Table<K>> extends NumberColumn<Double,K> {

    public DoubleColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, Double.class, nullable, size, decimalDigits, isGenerated);
    }

}
