package io.github.tatagulov.eq.metadata.api;

import io.github.tatagulov.eq.metadata.exception.TableNotFoundException;

import java.util.LinkedList;
import java.util.List;

public class Schema {

    public final DataBase dataBase;
    public final String schemaName;
    public final List<Table> tables = new LinkedList<Table>();
    public final List<Sequence> sequences= new LinkedList<Sequence>();

    public Schema(DataBase dataBase,String schemaName) {
        this.dataBase = dataBase;
        this.schemaName = schemaName;
        dataBase.schemas.add(this);
    }

    public Table getTable(String tableName) throws TableNotFoundException {
        Table table = findTable(tableName);
        if (table==null) throw new TableNotFoundException(tableName);
        return table;
    }

    public Table findTable(String tableName) {
        for (Table table : tables) {
            if (table.tableName.equalsIgnoreCase(tableName)) return table;
        }
        return null;
    }
}
