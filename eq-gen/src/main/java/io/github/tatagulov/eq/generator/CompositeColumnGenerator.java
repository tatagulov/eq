package io.github.tatagulov.eq.generator;

import io.github.tatagulov.eq.generator.exception.GenerateException;
import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.CompositeColumn;
import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.sql.JoinType;
import io.github.tatagulov.eq.metadata.sql.SQLColumn;
import io.github.tatagulov.eq.metadata.sql.SelectCompositeColumn;

import java.util.List;


public class CompositeColumnGenerator extends Template {

    private CompositeColumn compositeColumn;
    private final String columnClassName;

    public CompositeColumnGenerator(CompositeColumn<?> compositeColumn, String tableClassName) throws GenerateException {
        this.compositeColumn = compositeColumn;

        String selectCompositeColumnClassName = getClassName(SelectCompositeColumn.class);
        String selectColumnClassName = getClassName(SQLColumn.class);
        columnClassName = Utils.toCamelCase(compositeColumn.getName())+"Column";
        getClassName(Column.class);
        add("\tpublic class %s extends %s<%s,String> {\n", columnClassName, selectCompositeColumnClassName,tableClassName);
        add("\t\tpublic %s(%s table, %s ... columns) {\n", columnClassName,tableClassName,selectColumnClassName);
        add("\t\t\tsuper(table, columns);\n");
        add("\t\t}\n");

        if (compositeColumn.parentCompositeColumn!=null) {
            getLink(compositeColumn.parentCompositeColumn, JoinType.inner,false);
        }
        for (CompositeColumn childCompositeColumn : compositeColumn.childCompositeColumns) {
            boolean isMany = isMany(childCompositeColumn,compositeColumn.childCompositeColumns);
            getLink(childCompositeColumn,JoinType.inner,isMany);
            getLink(childCompositeColumn,JoinType.left,isMany);
        }

        add("\t}\n");
        add("\n");
    }

    private boolean isMany(CompositeColumn childCompositeColumn, List<CompositeColumn> childCompositeColumns) {
        for (CompositeColumn column : childCompositeColumns) {
            if (column!=childCompositeColumn && column.table == childCompositeColumn.table) return true;
        }
        return false;
    }

    protected void getLink(CompositeColumn compositeColumn, JoinType joinType, boolean isMany) throws GenerateException {
        String linkTableClassName = Utils.toCamelCase(compositeColumn.table.tableName );
        String methodName = joinType.name() + linkTableClassName;
        if (isMany) methodName += "By" + Utils.toCamelCase(compositeColumn.getName());

        String tableVarName = methodName + "Table";

        add("\t\tprivate %s %s;\n", linkTableClassName, tableVarName);
        add("\t\tpublic %s %s(){\n",linkTableClassName,methodName);
        add("\t\t\tif (%s == null) {\n",tableVarName);
        add("\t\t\t\t%s = new %s();\n",tableVarName,linkTableClassName);
        add("\t\t\t\tjoin(JoinType.%s,this, %s.%s);\n",joinType.name(),tableVarName,compositeColumn.getName());
        add("\t\t\t}\n");
        add("\t\t\treturn %s;\n",tableVarName);
        add("\t\t}\n");
    }

    private String getTableVarName(Table table) {
        String databaseVarName = Utils.toCamelCaseFirstLower(table.schema.dataBase.databaseName);
        String schemaVarName = table.schema.schemaName == null ? null :Utils.toCamelCaseFirstLower(table.schema.schemaName);
        String tableVarName = Utils.toCamelCaseFirstLower(table.tableName);
        return databaseVarName+"."+(schemaVarName==null?"": schemaVarName + ".")+tableVarName;
    }

    public String getVarText() {
        String varName = compositeColumn.getName();
        StringBuilder columnNames = new StringBuilder();


        for (Column column : compositeColumn.columns) {
            if (columnNames.length()>0) columnNames.append(",");
            String columnVarName = column.columnName;
            columnNames.append(columnVarName);
        }

        return String.format("\tpublic final %s %s = new %s(this,%s);\n",
                columnClassName,
                varName,
                columnClassName,
                columnNames);
    }
}
