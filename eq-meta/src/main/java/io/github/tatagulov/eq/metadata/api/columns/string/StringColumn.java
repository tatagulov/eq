package io.github.tatagulov.eq.metadata.api.columns.string;

import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.Table;

public class StringColumn<K extends Table<K>> extends Column<String,K> {

    public StringColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, String.class, nullable, size, decimalDigits, isGenerated);
    }

}
