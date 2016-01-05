package io.github.tatagulov.eq.metadata.sql;

public class When<T> extends Otherwise<T> {


    public When when(Condition condition,Expression<T> expression){
        condition.conditionType = ConditionType.when;

        WhenCondition whenCondition = new WhenCondition();
        whenCondition.condition = condition;
        whenCondition.expression = expression;
        whenConditions.add(whenCondition);
        return this;
    }

    public Otherwise<T> otherwise(Expression<T> otherwiseExpression) {
        this.otherwiseExpression = otherwiseExpression;
        return this;
    }
}
