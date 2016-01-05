package io.github.tatagulov.eq.metadata.sql;

import java.util.LinkedList;
import java.util.List;

public abstract class Condition {

    protected final Expression[] expressions;
    ConditionType conditionType;
    final List<Condition> childConditions = new LinkedList<Condition>();

    public Condition(Expression ... expressions) {
        this.expressions = expressions;
    }

    public String getSQL(AliasGenerator aliasGenerator) {
        StringBuilder sb = new StringBuilder();
        for (Condition childCondition : childConditions) {
            sb.append(childCondition.getSQL(aliasGenerator));
        }
        if (childConditions.size()>0 && (conditionType == ConditionType.and || conditionType== ConditionType.or)) {
            return String.format(" %s (%s%s)", conditionType.name(), getConditionSQL(aliasGenerator), sb);
        } else {
            return String.format(" %s %s%s", conditionType.name(),getConditionSQL(aliasGenerator), sb);
        }
    }

    protected abstract String getConditionSQL(AliasGenerator aliasGenerator);

    public List<From> getFrom() {
        List<From> froms = new LinkedList<From>();
        for (Expression<?> expression : expressions) {
            froms.addAll(expression.getFrom());
        }
        return froms;
    }

    public Condition and(Condition condition) {
        condition.conditionType = ConditionType.and;
        childConditions.add(condition);
        return this;
    }
    public Condition or(Condition condition) {
        condition.conditionType = ConditionType.or;
        childConditions.add(condition);
        return this;
    }

    public List<ParamExpression> getParamExpressions() {
        List<ParamExpression> paramExpressions = new LinkedList<ParamExpression>();
        for (Expression<?> expression : expressions) {
            paramExpressions.addAll(expression.getParamExpressions());
        }
        for (Condition childCondition : childConditions) {
            paramExpressions.addAll(childCondition.getParamExpressions());
        }
        return paramExpressions;
    }

    public boolean isAggregate() {
        for (Expression expression : expressions) {
            if (expression.isAggregate()) return true;
        }
        return false;
    }

    public List<Expression> getGroupByExpressions() {
        List<Expression> groupByExpressions = new LinkedList<Expression>();
        for (Expression<?> expression : expressions) {
            groupByExpressions.addAll(expression.getGroupByExpressions());
        }
        return groupByExpressions;
    }


    //    public static <K> Condition eqParam(Expression<K> left,Expression<K> right) {
//        BiBooleanExpression biBooleanExpression = new BiBooleanExpression(left, Criteria.EQ, right);
//        return new Condition(biBooleanExpression);
//    }
//
//    public static <K> Condition gt(Expression<K> left,Expression<K> right) {
//        BiBooleanExpression biBooleanExpression = new BiBooleanExpression(left, Criteria.GT, right);
//        return new Condition(biBooleanExpression);
//    }
//
//    public static <K> Condition gt_eq(Expression<K> left,Expression<K> right) {
//        BiBooleanExpression biBooleanExpression = new BiBooleanExpression(left, Criteria.GT_EQ, right);
//        return new Condition(biBooleanExpression);
//    }
//
//    public static <K> Condition ls_eq(Expression<K> left,Expression<K> right) {
//        BiBooleanExpression biBooleanExpression = new BiBooleanExpression(left, Criteria.LS_EQ, right);
//        return new Condition(biBooleanExpression);
//    }
//
//    public static Condition like(Expression<String> left,Expression<String> right) {
//        BiBooleanExpression biBooleanExpression = new BiBooleanExpression(left, Criteria.LIKE, right);
//        return new Condition(biBooleanExpression);
//    }
//
//    public static Condition isNull(Expression<String> left) {
//        IsNullBooleanTypeExpression biBooleanExpression = new IsNullBooleanTypeExpression(left);
//        return new Condition(biBooleanExpression);
//    }

//    public void fillValues(List<Object> values) {
//        fillValues(values, booleanExpression.getChildExpressions());
//
//        for (Condition childCondition: childConditions) {
//            childCondition.fillValues(values);
//        }
//    }
//
//    private static void fillValues(List<Object> values, List<Expression> selectExpressions) {
//        for (Expression expression : selectExpressions) {
//            if (expression instanceof ParamExpression) {
//                ParamExpression paramExpression = (ParamExpression) expression;
//                values.add(paramExpression.value);
//            }
//        }
//    }


//    public void fillParamExpressions(List<ParamExpression> paramExpressions) {
//        fillParamExpressions(paramExpressions, booleanExpression.getChildExpressions());
//        for (Condition childCondition : childConditions) {
//            childCondition.fillParamExpressions(paramExpressions);
//        }
//    }
//
//    private static void fillParamExpressions(List<ParamExpression> paramExpressions, List<Expression> childExpressions) {
//        for (Expression childExpression : childExpressions) {
//            if (childExpression instanceof ParamExpression) {
//                ParamExpression paramExpression = (ParamExpression) childExpression;
//                paramExpressions.add(paramExpression);
//            }
//        }
//    }
}
