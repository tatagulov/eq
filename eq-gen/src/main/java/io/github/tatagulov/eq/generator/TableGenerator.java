package io.github.tatagulov.eq.generator;

import io.github.tatagulov.eq.generator.exception.GenerateException;
import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.CompositeColumn;
import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.sql.SQLTable;
import io.github.tatagulov.eq.metadata.sql.JoinType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TableGenerator<K extends Table<K>> extends Template {

    private final String tableClassName;
    private final K table;
    private final String tablePath;

    public TableGenerator(DataBaseGenerator dataBaseGenerator, K table, String tablePackage, String tablePath) throws GenerateException {
        this.table = table;
        this.tablePath = tablePath;
        tableClassName = Utils.toCamelCase(table.tableName);
        getClassName(JoinType.class);
        String columnSection = getColumnSection(table, tableClassName);
        String tableClass = getClassName(SQLTable.class);
        String importSection = getImportSection();

        final String tableVarName = getTableVarName();

        add("package %s;\n\n", tablePackage);
        add(dataBaseGenerator.getStaticImport());
        add(importSection);
        add("public class %s extends %s<%s> {\n", tableClassName, tableClass,tableClassName);
        add("\tpublic %s() { super(%s); }\n", tableClassName, tableVarName);
        add("\n");
        add(columnSection);
        add("}\n");
    }

    public String getColumnSection(K table, String tableClassName) throws GenerateException {
        StringBuilder classes = new StringBuilder();
        StringBuilder variables = new StringBuilder();

        for (Column column : table.columns) {
            ColumnGenerator columnGenerator = new ColumnGenerator(column,tableClassName);
            if (columnGenerator.generated) {
                classes.append(columnGenerator.getText());
            }
            classNames.addAll(columnGenerator.classNames);
            variables.append(columnGenerator.getVarText());
        }

        for (CompositeColumn compositeColumn : table.compositeColumns) {
            CompositeColumnGenerator generator = new CompositeColumnGenerator(compositeColumn,tableClassName);
            classes.append(generator.getText());
            classNames.addAll(generator.classNames);
            variables.append(generator.getVarText());
        }
        return classes.toString() + variables.toString();
    }

    private String getTableVarName() {
        String databaseVarName = Utils.toCamelCaseFirstLower(table.schema.dataBase.databaseName);
        String schemaVarName = Utils.toCamelCaseFirstLower(table.schema.schemaName);
        String tableVarName = Utils.toCamelCaseFirstLower(table.tableName);
        return databaseVarName+"."+schemaVarName+"." +tableVarName;
    }

    public void save() throws IOException {
        File file = new File(tablePath + File.separator + tableClassName + ".java");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(getText().getBytes("UTF-8"));
        fileOutputStream.close();
    }
}
