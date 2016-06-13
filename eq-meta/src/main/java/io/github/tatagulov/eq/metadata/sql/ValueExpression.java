package io.github.tatagulov.eq.metadata.sql;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ValueExpression<T> implements Expression<T> {

    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd.MM.yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
    private static final DateTimeFormatter timeStampFormatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss.SSSSSS");

    public final T value;
    private final Class<T> type;

    public ValueExpression(T value, Class<T> type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public String getSQL(AliasGenerator aliasGenerator) {
        if (value==null) return "null";
        if (value instanceof String) return "'"+value.toString()+"'";
        if (value instanceof Date) return "'" +dateFormatter.print(((Date)value).getTime()) + "'";
        if (value instanceof Time) return "'" +timeFormatter.print(((Time)value).getTime()) + "'";
        if (value instanceof Timestamp) return "'" +timeStampFormatter.print(((Timestamp)value).getTime()) + "'";

        if (value instanceof Number) return value.toString();
        if (value instanceof Boolean) return value.toString();
        String message = String.format("convert for class %s not implement",type.getName());
        throw new RuntimeException(message);
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public List<ParamExpression> getParamExpressions() {
        return Collections.emptyList();
    }

    @Override
    public List<From> getFrom() {
        return new LinkedList<From>();
    }

    @Override
    public boolean isAggregate() {
        return false;
    }

    @Override
    public List<Expression> getGroupByExpressions() {
        return Collections.emptyList();
    }

    public static ValueExpression<Date> value(Date value) {
        return new ValueExpression<Date>(value,Date.class);
    }

    public static ValueExpression<Time> value(Time value) {
        return new ValueExpression<Time>(value,Time.class);
    }

    public static ValueExpression<Timestamp> value(Timestamp value) {
        return new ValueExpression<Timestamp>(value,Timestamp.class);
    }

    public static ValueExpression<BigDecimal> value(BigDecimal value) {
        return new ValueExpression<BigDecimal>(value,BigDecimal.class);
    }
    public static ValueExpression<Byte> value(Byte value) {
        return new ValueExpression<Byte>(value,Byte.class);
    }

    public static ValueExpression<Double> value(Double value) {
        return new ValueExpression<Double>(value,Double.class);
    }
    public static ValueExpression<Float> value(Float value) {
        return new ValueExpression<Float>(value,Float.class);
    }

    public static ValueExpression<Integer> value(Integer value) {
        return new ValueExpression<Integer>(value,Integer.class);
    }

    public static ValueExpression<Long> value(Long value) {
        return new ValueExpression<Long>(value,Long.class);
    }

    public static ValueExpression<Short> value(Short value) {
        return new ValueExpression<Short>(value,Short.class);
    }

    public static ValueExpression<Boolean> value(Boolean value) {
        return new ValueExpression<Boolean>(value,Boolean.class);
    }

    public static ValueExpression<byte[]> value(byte[] value) {
        return new ValueExpression<byte[]>(value,byte[].class);
    }

    public static ValueExpression<String> value(String value) {
        return new ValueExpression<String>(value,String.class);
    }
}
