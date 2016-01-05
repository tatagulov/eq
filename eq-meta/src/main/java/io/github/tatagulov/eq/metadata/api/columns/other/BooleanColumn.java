package io.github.tatagulov.eq.metadata.api.columns.other;

import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.Table;

public class BooleanColumn<K extends Table<K>> extends Column<Boolean,K> {

    public BooleanColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, Boolean.class, nullable, size, decimalDigits, isGenerated);
    }

}
