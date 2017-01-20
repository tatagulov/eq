package io.github.tatagulov.eq.metadata.exp;

import io.github.tatagulov.eq.metadata.sql.api.Select;

import java.sql.ResultSet;

public interface SQLExecutor {
    ResultSet execute(Select select);
}
