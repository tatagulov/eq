package io.github.tatagulov.eq.generator;

import io.github.tatagulov.eq.generator.exception.GenerateException;
import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.CompositeColumn;
import io.github.tatagulov.eq.metadata.api.Schema;
import io.github.tatagulov.eq.metadata.api.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MDTableGenerator<K extends Table<K>> extends Template {

    private final String tableClassName;
    private final K table;
    private final String tablePackage;
    private final String tablePath;

    public MDTableGenerator(DataBaseGenerator dataBaseGenerator, K table, String tablePackage, String tablePath) throws GenerateException {
        this.table = table;
        this.tablePackage = tablePackage;
        this.tablePath = tablePath;
        tableClassName = "MD" +Utils.toCamelCase(table.tableName);
        String columnSection = getColumnSection(table, tableClassName);
        String tableClass = getClassName(Table.class);
        String schemaClass = getClassName(Schema.class);
        String importSection = getImportSection();

        add("package %s;\n\n",tablePackage);
        add(dataBaseGenerator.getStaticImport());
        add(importSection);
        add("public class %s extends %s<%s> {\n", tableClassName, tableClass, tableClassName);
        add("\tpublic %s(%s schema) { super(schema, \"%s\"); }\n", tableClassName, schemaClass, table.tableName);
        add(columnSection);
        add("}\n");
    }


    public String getVarText() {
        String tableVarName = Utils.toCamelCaseFirstLower(table.tableName);
        return String.format("\tpublic final %s %s = new %s(this);\n",tableClassName,tableVarName,tableClassName);
    }

    public String getColumnSection(K table, String tableClassName) throws GenerateException {
        StringBuilder variables = new StringBuilder();

        for (Column column : table.columns) {
            MDColumnGenerator columnGenerator = new MDColumnGenerator(column,tableClassName);
            classNames.addAll(columnGenerator.classNames);
            variables.append(columnGenerator.getVarText());
        }

        for (CompositeColumn compositeColumn: table.compositeColumns) {
            MDCompositeColumnGenerator columnGenerator = new MDCompositeColumnGenerator(compositeColumn,tableClassName);
            classNames.addAll(columnGenerator.classNames);
            variables.append(columnGenerator.getVarText());
        }

        return variables.toString();
    }


    public String getFullClassName() {
        return tablePackage + "." + tableClassName;
    }

    public void save() throws IOException {
        File file = new File(tablePath + File.separator + tableClassName + ".java");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(getText().getBytes("UTF-8"));
        fileOutputStream.close();
    }
}
