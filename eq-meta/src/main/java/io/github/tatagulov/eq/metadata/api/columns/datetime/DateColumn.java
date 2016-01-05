package io.github.tatagulov.eq.metadata.api.columns.datetime;

import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.Table;

import java.sql.Date;

public class DateColumn<K extends Table<K>> extends Column<Date,K> {

    public DateColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, Date.class, nullable, size, decimalDigits, isGenerated);
    }
}
