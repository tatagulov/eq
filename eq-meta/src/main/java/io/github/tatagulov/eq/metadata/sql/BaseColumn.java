package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.sql.api.Select;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseColumn<V extends From,T> implements AliasExpression<T> {

    protected final V from;

    public BaseColumn(V from) {
        this.from = from;
    }

    public V getSelectTable() {
        return from;
    }

    @Override
    public List<From> getFrom() {
        List<From> froms = new LinkedList<From>();
        froms.add(from);
        return froms;
    }

    @Override
    public List<ParamExpression> getParamExpressions() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAggregate() {
        return false;
    }

    @Override
    public List<Expression> getGroupByExpressions() {
        return Collections.singletonList((Expression) this);
    }


    public Condition eq(Expression<T> expression) {
        return new TwoCondition<T>(this,Criteria.EQ,expression);
    }

    public Condition eq(T value) {
        return eq(new ParamExpression<T>(value,getType()));
    }

    public Condition notEq(Expression<T> expression) {
        return new TwoCondition<T>(this,Criteria.NOT_EQ,expression);
    }

    public Condition notEq(T value) {
        return notEq(new ParamExpression<T>(value,getType()));
    }

    public Condition gtEq(Expression<T> expression) {
        return new TwoCondition<T>(this,Criteria.GT_EQ,expression);
    }

    public Condition gtEq(T value) {
        return gtEq(new ParamExpression<T>(value,getType()));
    }

    public Condition gt(Expression<T> expression) {
        return new TwoCondition<T>(this,Criteria.GT,expression);
    }

    public Condition gt(T value) {
        return gt(new ParamExpression<T>(value,getType()));
    }

    public Condition lsEq(Expression<T> expression) {
        return new TwoCondition<T>(this,Criteria.LS_EQ,expression);
    }

    public Condition lsEq(T value) {
        return lsEq(new ParamExpression<T>(value,getType()));
    }

    public Condition ls(Expression<T> expression) {
        return new TwoCondition<T>(this,Criteria.LS,expression);
    }

    public Condition ls(T value) {
        return ls(new ParamExpression<T>(value,getType()));
    }

    public Condition isNull() {
        return new IsNullCondition(this);
    }

    public Condition isNotNull() {
        return new IsNotNullCondition(this);
    }

    public Condition in(Expression<T> ... expressions) {
        return new InCondition(this,expressions);
    }

    public Condition notIn(Expression ... expressions) {
        return new NotInCondition(this,expressions);
    }

    public Condition in(Select select) {
        return new InCondition(this, select.asExpression());
    }

    public Condition notIn(Select select) {
        return new NotInCondition(this, select.asExpression());
    }
}
