package io.github.tatagulov.eq.metadata.metadata;

import io.github.tatagulov.eq.metadata.api.*;
import io.github.tatagulov.eq.metadata.api.columns.datetime.DateColumn;
import io.github.tatagulov.eq.metadata.api.columns.datetime.TimeColumn;
import io.github.tatagulov.eq.metadata.api.columns.datetime.TimestampColumn;
import io.github.tatagulov.eq.metadata.api.columns.number.*;
import io.github.tatagulov.eq.metadata.api.columns.other.BooleanColumn;
import io.github.tatagulov.eq.metadata.api.columns.other.ByteArrayColumn;
import io.github.tatagulov.eq.metadata.api.columns.string.StringColumn;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class JDBCDatabaseExtractor implements DatabaseExtractor{

    @Override
    public DataBase extract(Connection connection,String databaseName,String schemaPattern) throws SQLException {
        DataBase dataBase = new DataBase(databaseName);
        fillTables(connection,schemaPattern,dataBase);
        fillReferences(connection,dataBase);
        fillTransitiveReferences(dataBase);
        return dataBase;
    }

    protected void fillTransitiveReferences(DataBase dataBase) {
        for (Schema schema : dataBase.schemas) {
            for (Table<?> table : schema.tables) {
                for (CompositeColumn compositeColumn : table.compositeColumns) {
                    for (int i = 0; i < compositeColumn.columns.length; i++) {
                        Column column = compositeColumn.columns[i];
                        if (column.parentColumn==null) {
                            Column parentColumn = findParentColumn(i, compositeColumn);
                            if (parentColumn!=null) {
                                dataBase.references.add(new ColumnReference(parentColumn,column,false));
                            }
                        }
                    }
                }
            }
        }
    }

    protected void fillTables(Connection connection, String schemaPattern,DataBase dataBase) throws SQLException {
        ResultSet rs = connection.getMetaData().getTables(null,schemaPattern,null,new String[]{"TABLE"});
        while (rs.next()) {
            String schemaName = rs.getString("TABLE_SCHEM");
            String tableName = rs.getString("TABLE_NAME");
            Schema schema = dataBase.findSchema(schemaName);
            if (schema==null) schema = new Schema(dataBase, schemaName);
            Table table = new Table(schema, tableName);
            fillColumn(connection,table);
        }
        rs.close();
    }

    protected void fillColumn(Connection connection,Table table) throws SQLException {
        List<String> primaryColumnNames = getPrimaryKeyNames(connection,table);
        ResultSet resultSet = connection.getMetaData().getColumns(null, table.schema.schemaName, table.tableName, null);

        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            boolean isPk = primaryColumnNames.contains(columnName);
            int sqlType = resultSet.getInt("DATA_TYPE");
            boolean nullable = (resultSet.getInt("NULLABLE") == ResultSetMetaData.columnNullable);
            int size = resultSet.getInt("COLUMN_SIZE");
            int decimalDigits = resultSet.getInt("DECIMAL_DIGITS");
//            boolean isGenerated = resultSet.getString("IS_GENERATEDCOLUMN").equals("YES");
            boolean isGenerated = resultSet.getString("IS_AUTOINCREMENT").equals("YES");
            createColumnBySQLType(sqlType,table,columnName, isPk,nullable,size, decimalDigits, isGenerated);
        }
        resultSet.close();
    }


    public static <K extends Table<K>> Column<?,K> createColumnBySQLType(int type, K table, String columnName, boolean isPK, boolean nullable, int size, int decimalDigits, boolean isGenerated) throws SQLException {
        switch( type ) {
            case Types.BOOLEAN:
                return new BooleanColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.CLOB :
                return new StringColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.NUMERIC:
            case Types.DECIMAL:
                return new BigDecimalColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.BIT:
                return new BooleanColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.TINYINT:
                return new ByteColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.SMALLINT:
                return new ShortColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.INTEGER:
                return new IntegerColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.BIGINT:
                return new LongColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.REAL:
            case Types.FLOAT:
                return new FloatColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.DOUBLE:
                return new DoubleColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB :
                return new ByteArrayColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.DATE:
                return new DateColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.TIME:
                return new TimeColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            case Types.TIMESTAMP:
                return new TimestampColumn<K>(table,columnName, isPK,nullable, size, decimalDigits, isGenerated);
            default:
                String text =String.format("for type %s class not found",type);
                throw new SQLException(text);
        }
    }

    protected List<String> getPrimaryKeyNames(Connection connection,Table table) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getPrimaryKeys(null, table.schema.schemaName, table.tableName);
        SortedMap<Short,String> columnMap = new TreeMap<Short,String>();
        while (resultSet.next()) {
            columnMap.put(resultSet.getShort("KEY_SEQ"), resultSet.getString("COLUMN_NAME"));
        }
        resultSet.close();

        List<String> primaryKeyNames = new LinkedList<String>();
        if (!columnMap.isEmpty()) {
            for (String columnName :columnMap.values()) {
                primaryKeyNames.add(columnName);
            }
        }
        return primaryKeyNames;
    }

    protected void fillReferences(Connection connection,DataBase dataBase) throws SQLException {

        for (Schema schema : dataBase.schemas) {
            for (Table table : schema.tables) {
                ResultSet resultSet = connection.getMetaData().getImportedKeys(null, schema.schemaName, table.tableName);
                short oldKeySeq = 0;
                List<Column> pkColumns = new LinkedList<Column>();
                List<Column> fkColumns = new LinkedList<Column>();
                while (resultSet.next()) {
                    String pkSchemaName = resultSet.getString("PKTABLE_SCHEM");
                    String pkTableName = resultSet.getString("PKTABLE_NAME");
                    String pkColumnName = resultSet.getString("PKCOLUMN_NAME");

                    String fkSchemaName = resultSet.getString("FKTABLE_SCHEM");
                    String fkTableName = resultSet.getString("FKTABLE_NAME");
                    String fkColumnName = resultSet.getString("FKCOLUMN_NAME");
                    short keySeq = resultSet.getShort("KEY_SEQ");

                    if (oldKeySeq>=keySeq) {
                        createDBReference(pkColumns, fkColumns,dataBase);
                        pkColumns.clear();
                        fkColumns.clear();
                    }
                    oldKeySeq = keySeq;

                    Column pkColumn = dataBase.findSchema(pkSchemaName).findTable(pkTableName).findColumn(pkColumnName);
                    Column fkColumn = dataBase.findSchema(fkSchemaName).findTable(fkTableName).findColumn(fkColumnName);

                    pkColumns.add(pkColumn);
                    fkColumns.add(fkColumn);
                }
                resultSet.close();
                if (oldKeySeq>0) createDBReference(pkColumns, fkColumns,dataBase);
            }
        }
    }

    protected Column findParentColumn(int index, CompositeColumn compositeColumn) {
        CompositeColumn parentCompositeColumn = compositeColumn.parentCompositeColumn;
        if (parentCompositeColumn ==null) return null;

        Column<?,? extends Table<?>> linkColumn = parentCompositeColumn.columns[index];
        if (linkColumn.parentColumn!=null) return linkColumn.parentColumn;

        for (CompositeColumn otherCompositeColumn : linkColumn.table.compositeColumns) {
            Column[] columns = otherCompositeColumn.columns;
            for (int i = 0; i < columns.length; i++) {
                Column column = columns[i];
                if (column == linkColumn) {
                    Column parentColumn = findParentColumn(i, otherCompositeColumn);
                    if (parentColumn!=null) return parentColumn;
                }
            }
        }
        return null;
    }

    protected void createDBReference(List<Column> pkColumns, List<Column> fkColumns,DataBase dataBase) throws SQLException {
        if (pkColumns.size()==1 && fkColumns.size()==1) {
            Column pkColumn = pkColumns.get(0);
            Column fkColumn = fkColumns.get(0);
            dataBase.references.add(new ColumnReference(pkColumn,fkColumn,true));
        } else {

            Column[] pk = pkColumns.toArray(new Column[pkColumns.size()]);
            Column[] fk = fkColumns.toArray(new Column[fkColumns.size()]);
            CompositeColumn pkCompositeColumn = pk[0].table.getCompositeColumn(pk);
            CompositeColumn fkCompositeColumn = fk[0].table.getCompositeColumn(fk);

            dataBase.references.add(new CompositeColumnReference(pkCompositeColumn,fkCompositeColumn));
        }
    }
}
