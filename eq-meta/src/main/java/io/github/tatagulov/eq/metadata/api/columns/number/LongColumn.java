package io.github.tatagulov.eq.metadata.api.columns.number;

import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.api.columns.NumberColumn;

public class LongColumn<K extends Table<K>> extends NumberColumn<Long,K> {

    public LongColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, Long.class, nullable, size, decimalDigits, isGenerated);
    }

}
