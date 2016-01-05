package io.github.tatagulov.eq.metadata.api.columns.number;

import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.api.columns.NumberColumn;

import java.math.BigDecimal;

public class BigDecimalColumn<K extends Table<K>> extends NumberColumn<BigDecimal,K> {

    public BigDecimalColumn(K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) {
        super(table, columnName, isPK, BigDecimal.class, nullable, size, decimalDigits, isGenerated);
    }

}
