package io.github.tatagulov.eq.metadata.api.columns.datetime;

import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.Table;

import java.sql.Time;

public class TimeColumn<K extends Table<K>> extends Column<Time,K> {

    public TimeColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, Time.class, nullable, size, decimalDigits, isGenerated);
    }

}
