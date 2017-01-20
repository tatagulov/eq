package io.github.tatagulov.eq.metadata.exp;

import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.exception.ObjectNotFoundException;
import io.github.tatagulov.eq.metadata.sql.*;

import java.util.LinkedList;
import java.util.List;

public class StringExpression implements Expression {

    protected final String stringExpression;
    protected final List<SQLColumn> expressions = new LinkedList<SQLColumn>();

    public StringExpression(Table baseTable, String stringExpression) throws ObjectNotFoundException {
        this.stringExpression = Parser.parse(baseTable,stringExpression,expressions);
    }


    @Override
    public Class getType() {
        return null;
    }

    @Override
    public List<From> getFrom() {
        return null;
    }

    @Override
    public List<ParamExpression> getParamExpressions() {
        return null;
    }

    @Override
    public boolean isAggregate() {
        return false;
    }

    @Override
    public List<Expression> getGroupByExpressions() {
        return null;
    }

    @Override
    public String getSQL(AliasGenerator aliasGenerator) {
        List<String> strings = new LinkedList<String>();
        for (SQLColumn expression : expressions) {
            String sql = expression.getSQL(aliasGenerator);
            strings.add(sql);
        }
        return String.format(stringExpression, strings.toArray(new String[strings.size()]));
    }
}
