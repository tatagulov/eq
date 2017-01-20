package io.github.tatagulov.eq.metadata.exp;

import io.github.tatagulov.eq.metadata.sql.Expression;
import io.github.tatagulov.eq.metadata.sql.PostgresSelect;
import io.github.tatagulov.eq.metadata.sql.api.Select;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public abstract class Setter<K> {

    private Select select = new PostgresSelect();
    private ResultSet resultSet = null;

    protected abstract K map();

    protected <T> T val(Expression<T> expression) {
        try {
            List<Expression> expressions = select.getSelectExpressions();
            if (resultSet==null) {
                select.select(expression);
                return null;
            } else {
                int index = expressions.indexOf(expression) + 1;
                return (T) resultSet.getObject(index);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<K> execute(SQLExecutor SQLExecutor) throws SQLException {
        map();
        resultSet = SQLExecutor.execute(select);
        List<K> list = new LinkedList<K>();
        while (resultSet.next()) {
            K value = map();
            list.add(value);
        }
        return list;
    }
}
