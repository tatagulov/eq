package io.github.tatagulov.eq.metadata.sql;

public enum Criteria {
    EQ("="),
    GT(">"),
    GT_EQ(">="),
    LS("<"),
    LS_EQ("<="),
    LIKE(" like ");

    public final String sql;

    Criteria(String sql) {
        this.sql = sql;
    }
}
