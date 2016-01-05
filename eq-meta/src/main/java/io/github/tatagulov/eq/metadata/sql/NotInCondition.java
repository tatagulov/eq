package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.sql.AliasGenerator;
import io.github.tatagulov.eq.metadata.sql.Condition;
import io.github.tatagulov.eq.metadata.sql.Expression;

public class NotInCondition extends Condition {
    private final Expression expression;

    public NotInCondition(Expression expression,Expression ... expressions) {
        super(expressions);
        this.expression = expression;
    }

    @Override
    protected String getConditionSQL(AliasGenerator aliasGenerator) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Expression expression : expressions) {
            if (stringBuilder.length()>0) stringBuilder.append(",");
            stringBuilder.append(expression.getSQL(aliasGenerator));
        }
        return expression.getSQL(aliasGenerator) + " not in " + stringBuilder.toString();
    }
}
