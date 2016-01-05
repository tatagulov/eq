package io.github.tatagulov.eq.metadata.api.columns.datetime;

import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.Table;

import java.sql.Timestamp;

public class TimestampColumn<K extends Table<K>> extends Column<Timestamp,K> {

    public TimestampColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, Timestamp.class, nullable, size, decimalDigits, isGenerated);
    }
}
