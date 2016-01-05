package io.github.tatagulov.eq.metadata.api.columns.number;

import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.api.columns.NumberColumn;

public class ByteColumn<K extends Table<K>> extends NumberColumn<Byte,K> {

    public ByteColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, Byte.class, nullable, size, decimalDigits, isGenerated);
    }

}
