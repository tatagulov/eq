package io.github.tatagulov.eq.metadata.api.columns.number;

import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.api.columns.NumberColumn;

public class ShortColumn<K extends Table<K>> extends NumberColumn<Short,K> {

    public ShortColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, Short.class, nullable, size, decimalDigits, isGenerated);
    }

}
