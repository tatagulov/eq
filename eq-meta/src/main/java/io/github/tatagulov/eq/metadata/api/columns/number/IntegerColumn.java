package io.github.tatagulov.eq.metadata.api.columns.number;

import io.github.tatagulov.eq.metadata.api.columns.NumberColumn;
import io.github.tatagulov.eq.metadata.api.Table;

public class IntegerColumn<K extends Table<K>> extends NumberColumn<Integer,K> {

    public IntegerColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, Integer.class, nullable, size, decimalDigits, isGenerated);
    }

}
