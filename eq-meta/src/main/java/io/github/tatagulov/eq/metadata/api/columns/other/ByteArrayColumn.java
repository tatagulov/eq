package io.github.tatagulov.eq.metadata.api.columns.other;

import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.Table;

public class ByteArrayColumn<K extends Table<K>> extends Column<byte[],K> {

    public ByteArrayColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, byte[].class, nullable, size, decimalDigits, isGenerated);
    }

}
