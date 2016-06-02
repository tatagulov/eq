package io.github.tatagulov.eq.metadata.sql;

public class PostgresSelect extends BaseSelect {

    public PostgresSelect(boolean distinct) {
        super(distinct);
    }
    public PostgresSelect() {
        super(false);
    }
}
