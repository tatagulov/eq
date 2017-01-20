package io.github.tatagulov.eq.metadata.exp;

import io.github.tatagulov.eq.metadata.sql.api.Select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleSQLExecutor implements SQLExecutor {

    private final Connection connection;

    public SimpleSQLExecutor(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ResultSet execute(Select select) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(select.getSQL());
            Object[] values = select.getValues();
            int index = 1;
            for (Object value : values) {
                preparedStatement.setObject(index++,value);
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
