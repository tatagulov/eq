package io.github.tatagulov.eq.metadata.sql;

public class MySQLSelect extends BaseSelect {

    public MySQLSelect(boolean distinct) {
        super(distinct);
    }
    public MySQLSelect() {
        super(false);
    }
}
