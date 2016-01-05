package io.github.tatagulov.eq.metadata.sql;

public enum JoinType {
    inner("inner join"),
    left("left join"),
    right("right join"),
    full("full join");
    public final String sql;

    JoinType(String sql) {
        this.sql = sql;
    }
}
