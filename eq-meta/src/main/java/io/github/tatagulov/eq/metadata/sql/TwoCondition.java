package io.github.tatagulov.eq.metadata.sql;

public class TwoCondition<T> extends Condition {

    protected final Criteria criteria;

    public TwoCondition(Expression<? extends T> left,Criteria criteria,Expression<? extends T> right) {
        super(left, right);
        this.criteria = criteria;
    }

    @Override
    public String getConditionSQL(AliasGenerator aliasGenerator) {
        return String.format("%s %s %s", expressions[0].getSQL(aliasGenerator), criteria.sql, expressions[1].getSQL(aliasGenerator));
    }

    public static <K> TwoCondition<K> eq(Expression<? extends K> left,Expression<? extends K> right) {
        return new TwoCondition<K>(left,Criteria.EQ,right);
    }
}
