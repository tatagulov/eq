package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.sql.AliasGenerator;
import io.github.tatagulov.eq.metadata.sql.Condition;
import io.github.tatagulov.eq.metadata.sql.Expression;

public class IsNotNullCondition extends Condition {
    public IsNotNullCondition(Expression expression) {
        super(expression);
    }

    @Override
    protected String getConditionSQL(AliasGenerator aliasGenerator) {
        return String.format("%s is not null", expressions[0].getSQL(aliasGenerator));
    }
}
