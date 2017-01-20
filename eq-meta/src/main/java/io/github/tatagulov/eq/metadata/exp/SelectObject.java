package io.github.tatagulov.eq.metadata.exp;

import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.DataBase;
import io.github.tatagulov.eq.metadata.api.Schema;
import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.exception.SchemaNotFoundException;
import io.github.tatagulov.eq.metadata.exception.TableNotFoundException;
import io.github.tatagulov.eq.metadata.sql.PostgresSelect;
import io.github.tatagulov.eq.metadata.sql.SQLColumn;
import io.github.tatagulov.eq.metadata.sql.SQLTable;
import io.github.tatagulov.eq.metadata.sql.api.Select;

import java.lang.reflect.Field;

public class SelectObject {

    public <T>  T select(T object, DataBase dataBase) throws SchemaNotFoundException, TableNotFoundException {
        Class<?> clazz = object.getClass();
        EQTable eqTable = clazz.getAnnotation(EQTable.class);
        if (eqTable ==null) throw new RuntimeException("annotation EQTable not found");

        String schemaName = eqTable.schema();
        String tableName = eqTable.value();
        Schema schema = dataBase.getSchema(schemaName);
        Table<?> table = schema.getTable(tableName);

        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            EQColumn eqColumn = field.getAnnotation(EQColumn.class);
            if (eqColumn!=null) {
                String columnName = eqColumn.value();
            }
        }

        Select select = new PostgresSelect();


        SQLTable<?> sqlTable = new SQLTable(table);
        for (Column<?, ?> column : table.columns) {

        }
        return null;
    }


}
