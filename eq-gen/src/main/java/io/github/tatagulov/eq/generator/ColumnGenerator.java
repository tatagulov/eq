package io.github.tatagulov.eq.generator;


import io.github.tatagulov.eq.generator.exception.GenerateException;
import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.sql.SQLColumn;
import io.github.tatagulov.eq.metadata.sql.JoinType;

import java.util.List;

public class ColumnGenerator extends Template {

    public final boolean generated;
    private final Column column;
    private final String tableClassName;
    private final String columnClassName;
    private final String typeClassName;

    public ColumnGenerator(Column<?,?> column, String tableClassName) throws GenerateException {
        this.column = column;
        this.tableClassName = tableClassName;

        String columnExtendClassName = getClassName(SQLColumn.class);
        typeClassName = getClassName(column.type);

        getClassName(Column.class);

        generated = column.parentColumn!=null || column.childColumns.size()>0;

        if (generated) {
            columnClassName = Utils.toCamelCase(column.columnName)+"Column";
            add("\tpublic class %s extends %s<%s,%s> {\n", columnClassName, columnExtendClassName,tableClassName, typeClassName);
            add("\t\tpublic %s(%s table, Column<%s, ?> column) { super(table, column); }\n", columnClassName,tableClassName, typeClassName);

            if (column.parentColumn!=null) {
                getLink(column.parentColumn, JoinType.inner, false);
                if (column.nullable) getLink(column.parentColumn, JoinType.left, false);
            }

            for (Column childColumn : column.childColumns) {
                boolean isMany = isMany(childColumn,column.childColumns);
                getLink(childColumn,JoinType.inner,isMany);
                getLink(childColumn,JoinType.left,isMany);
            }
            add("\t}\n");
            add("\n");
        } else {
            columnClassName = columnExtendClassName;
        }
    }

    private boolean isMany(Column childColumn, List<Column> childColumns) {
        for (Column column : childColumns) {
            if (column!=childColumn && column.table == childColumn.table) return true;
        }
        return false;
    }

    protected void getLink(Column column, JoinType joinType, boolean isMany) throws GenerateException {
        String linkTableClassName = Utils.toCamelCase(column.table.tableName );
        String methodName = joinType.name() + linkTableClassName;
        if (isMany) methodName += "By" + Utils.toCamelCase(column.columnName);

        String tableVarName = methodName + "Table";

        add("\t\tprivate %s %s;\n",linkTableClassName,tableVarName);
        add("\t\tpublic %s %s(){\n",linkTableClassName,methodName);
        add("\t\t\tif (%s == null) {\n",tableVarName);
        add("\t\t\t\t%s = new %s();\n",tableVarName,linkTableClassName);
        add("\t\t\t\tjoin(JoinType.%s,this, %s.%s);\n",joinType.name(),tableVarName,column.columnName);
        add("\t\t\t}\n");
        add("\t\t\treturn %s;\n",tableVarName);
        add("\t\t}\n");
    }

    private String getColumnVarName() {
        String databaseVarName = Utils.toCamelCaseFirstLower(column.table.schema.dataBase.databaseName);
        String schemaVarName = column.table.schema.schemaName == null ? null : Utils.toCamelCaseFirstLower(column.table.schema.schemaName);
        String tableVarName = Utils.toCamelCaseFirstLower(column.table.tableName);
        return databaseVarName+"."+(schemaVarName==null?"": schemaVarName + ".") +tableVarName + "." +column.columnName;
    }

    public String getVarText() {
        String columnVarName = getColumnVarName();
        String generic1 = generated ? "" : String.format("<%s,%s>",tableClassName,typeClassName);
        return String.format("\tpublic final %s%s %s = new %s%s(this, %s);\n",
                columnClassName,
                generic1,
                column.columnName,
                columnClassName,
                generic1,
                columnVarName);
    }

}
