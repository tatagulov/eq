package io.github.tatagulov.eq.metadata.exp;

import io.github.tatagulov.eq.metadata.sql.Expression;
import io.github.tatagulov.eq.metadata.sql.PostgresSelect;
import io.github.tatagulov.eq.metadata.sql.api.Select;

public abstract class Setter {

    private boolean isPrepare = true;
    private Select select = new PostgresSelect();

    public abstract void select();

    public <T> T getValue(Expression<T> expression) {
        if (isPrepare) {
            select.select(expression);
        } else {

        }
    }
}
