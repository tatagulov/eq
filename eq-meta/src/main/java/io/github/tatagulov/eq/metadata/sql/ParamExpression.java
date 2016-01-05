package io.github.tatagulov.eq.metadata.sql;


import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ParamExpression<T> implements Expression<T> {
    public final T value;
    private final Class<T> type;

    public ParamExpression(T value, Class<T> type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public String getSQL(AliasGenerator aliasGenerator) {
        return "?";
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public List<ParamExpression> getParamExpressions() {
        return Collections.singletonList((ParamExpression)this);
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

    public SimpleAliasExpression<T> as(String alias) {
        return new SimpleAliasExpression<T>(this,alias);
    }


    public static ParamExpression<Date> param(Date param) {
        return new ParamExpression<Date>(param,Date.class);
    }

    public static ParamExpression<Time> param(Time param) {
        return new ParamExpression<Time>(param,Time.class);
    }

    public static ParamExpression<Timestamp> param(Timestamp param) {
        return new ParamExpression<Timestamp>(param,Timestamp.class);
    }

    public static ParamExpression<BigDecimal> param(BigDecimal param) {
        return new ParamExpression<BigDecimal>(param,BigDecimal.class);
    }
    public static ParamExpression<Byte> param(Byte param) {
        return new ParamExpression<Byte>(param,Byte.class);
    }

    public static ParamExpression<Double> param(Double param) {
        return new ParamExpression<Double>(param,Double.class);
    }
    public static ParamExpression<Float> param(Float param) {
        return new ParamExpression<Float>(param,Float.class);
    }

    public static ParamExpression<Integer> param(Integer param) {
        return new ParamExpression<Integer>(param,Integer.class);
    }

    public static ParamExpression<Long> param(Long param) {
        return new ParamExpression<Long>(param,Long.class);
    }

    public static ParamExpression<Short> param(Short param) {
        return new ParamExpression<Short>(param,Short.class);
    }

    public static ParamExpression<Boolean> param(Boolean param) {
        return new ParamExpression<Boolean>(param,Boolean.class);
    }

    public static ParamExpression<byte[]> param(byte[] param) {
        return new ParamExpression<byte[]>(param,byte[].class);
    }

    public static ParamExpression<String> param(String param) {
        return new ParamExpression<String>(param,String.class);
    }
}
