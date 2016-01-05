package io.github.tatagulov.eq.generator;


import io.github.tatagulov.eq.generator.exception.GenerateException;
import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.columns.datetime.DateColumn;
import io.github.tatagulov.eq.metadata.api.columns.datetime.TimeColumn;
import io.github.tatagulov.eq.metadata.api.columns.datetime.TimestampColumn;
import io.github.tatagulov.eq.metadata.api.columns.number.*;
import io.github.tatagulov.eq.metadata.api.columns.other.BooleanColumn;
import io.github.tatagulov.eq.metadata.api.columns.other.ByteArrayColumn;
import io.github.tatagulov.eq.metadata.api.columns.string.StringColumn;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class MDColumnGenerator extends Template {

    private final Column column;
    private final String tableClassName;
    private final String columnClassName;

    public MDColumnGenerator(Column<?, ?> column, String tableClassName) throws GenerateException {
        this.column = column;
        this.tableClassName = tableClassName;
        Class columnClass = getColumnClass(column.type);
        columnClassName = getClassName(columnClass);
    }

    public String getVarText() {
        return String.format("\tpublic final %s<%s> %s = new %s<%s>(this,\"%s\",%s,%s,%s,%s,%s);\n",
                columnClassName,
                tableClassName,
                column.columnName,
                columnClassName,
                tableClassName,
                column.columnName,
                column.isPK,
                column.nullable,
                column.size,
                column.decimalDigits,
                column.isGenerated);
    }

    private static Class getColumnClass(Class type) throws GenerateException {
        if (type==String.class) return StringColumn.class;

        if (type== Date.class) return DateColumn.class;
        if (type==Time.class) return TimeColumn.class;
        if (type==Timestamp.class) return TimestampColumn.class;

        if (type==BigDecimal.class) return BigDecimalColumn.class;
        if (type==Byte.class) return ByteColumn.class;
        if (type==Double.class) return DoubleColumn.class;
        if (type==Float.class) return FloatColumn.class;
        if (type==Integer.class) return IntegerColumn.class;
        if (type==Long.class) return LongColumn.class;
        if (type==Short.class) return ShortColumn.class;

        if (type==byte[].class) return ByteArrayColumn.class;
        if (type==Boolean.class) return BooleanColumn.class;
        throw new GenerateException(String.format("class %s not implement",type.getName()));
    }
}
