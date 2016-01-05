package io.github.tatagulov.eq.metadata.metadata;

import io.github.tatagulov.eq.metadata.api.DataBase;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseExtractor {
    DataBase extract(Connection connection,String databaseName,String schemaPattern) throws SQLException;
}
