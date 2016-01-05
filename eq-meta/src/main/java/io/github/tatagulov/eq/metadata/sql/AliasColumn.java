package io.github.tatagulov.eq.metadata.sql;

public class AliasColumn<V extends BaseSelect,K> extends BaseColumn<V,K> {


    private final AliasExpression<K> aliasExpression;

    public AliasColumn(V select, AliasExpression<K> aliasExpression) {
        super(select);
        select.selectExpressions.add(aliasExpression);
        this.aliasExpression = aliasExpression;
    }

    @Override
    public Class<K> getType() {
        return aliasExpression.getType();
    }

    @Override
    public String getSQL(AliasGenerator aliasGenerator) {
        return from.alias +"." + aliasExpression.getAlias();
    }

    @Override
    public String getAlias() {
        return aliasExpression.getAlias();
    }
}
