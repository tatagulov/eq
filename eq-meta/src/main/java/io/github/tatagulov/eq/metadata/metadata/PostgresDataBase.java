package io.github.tatagulov.eq.metadata.metadata;

import io.github.tatagulov.eq.metadata.api.DataBase;
import io.github.tatagulov.eq.metadata.api.Schema;
import io.github.tatagulov.eq.metadata.api.Sequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresDataBase extends JDBCDatabaseExtractor {

    @Override
    public DataBase extract(Connection connection, String databaseName, String schemaPattern) throws SQLException {
        DataBase dataBase = super.extract(connection, databaseName, schemaPattern);
        fillSequence(connection,schemaPattern,dataBase);
        return dataBase;
    }

    protected void fillSequence(Connection connection, String schemaPattern,DataBase dataBase) throws SQLException {
        String sql = "select * from information_schema.sequences where sequence_schema = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,schemaPattern);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Schema schema = dataBase.findSchema(rs.getString("sequence_schema"));
            new Sequence(schema,rs.getString("sequence_name"));
        }
        rs.close();
        ps.close();
    }
}
