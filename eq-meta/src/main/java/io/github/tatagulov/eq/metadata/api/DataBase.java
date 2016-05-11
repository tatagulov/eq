package io.github.tatagulov.eq.metadata.api;

import io.github.tatagulov.eq.metadata.exception.SchemaNotFoundException;
import io.github.tatagulov.eq.metadata.exception.TableNotFoundException;

import java.util.LinkedList;
import java.util.List;

public class DataBase {

    public final String databaseName;
    public final List<Schema> schemas = new LinkedList<Schema>();
    public final List<Reference> references = new LinkedList<Reference>();

    public DataBase(String databaseName) {
        this.databaseName = databaseName;
    }

    public Schema findSchema(String schemaName) {
        for (Schema schema : schemas) {
            if (schema.schemaName == null) return schema;
            if (schema.schemaName.equalsIgnoreCase(schemaName)) return schema;
        }
        return null;
    }

    public Schema getSchema(String schemaName) throws SchemaNotFoundException {
        Schema schema = findSchema(schemaName);
        if (schema==null) throw new SchemaNotFoundException(schemaName);
        return schema;
    }

    public Table getTable(String fullTableName) throws SchemaNotFoundException, TableNotFoundException {
        String[] items = fullTableName.split("\\.");
        String schemaName = items[0];
        String tableName = items[1];
        return getSchema(schemaName).getTable(tableName);
    }

    public Table findTable(String fullTableName) {
        String[] items = fullTableName.split("\\.");
        String schemaName = items[0];
        String tableName = items[1];
        Schema schema = findSchema(schemaName);
        if (schema==null) return null;
        return schema.findTable(tableName);
    }
}
