package io.github.tatagulov.eq.generator;

import io.github.tatagulov.eq.generator.exception.GenerateException;
import io.github.tatagulov.eq.metadata.api.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataBaseGenerator extends Template {

    private final DataBase dataBase;
    private final String dbPackage;

    public DataBaseGenerator(DataBase dataBase,String dbPackage,String path) throws IOException, GenerateException {
        this.dataBase = dataBase;
        this.dbPackage = dbPackage;
        generateManyClass(dataBase, dbPackage, path);
    }

    public String getStaticImport() {
        return "import static " + dbPackage + "." +Utils.toCamelCase(dataBase.databaseName) + ".*;\n";
    }

    private void generateManyClass(DataBase dataBase, String dbPackage, String path) throws IOException, GenerateException {

        File rootDir = new File(path);
        if (rootDir.exists()) deleteDirectory(rootDir);

        String dbPath = path + File.separator + Utils.replacePackageToPath(dbPackage);
        Utils.checkDir(dbPath);

        String schemaSection = getSchemaSection(dataBase, dbPackage, dbPath);
        String dbClassName = Utils.toCamelCase(dataBase.databaseName);
        String dataBaseClass = getClassName(DataBase.class);
        String dbVarName = Utils.toCamelCaseFirstLower(dataBase.databaseName);
        getClassName(Column.class);
        getClassName(ColumnReference.class);
        getClassName(CompositeColumnReference.class);

        String importSection = getImportSection();


        add("package %s;\n\n",dbPackage);
        add(importSection);
        add("public class %s extends %s {\n",dbClassName,dataBaseClass);
        add("\tpublic static final %s %s = new %s();\n",dbClassName,dbVarName,dbClassName);
        add(schemaSection);
        add("\tprivate %s() {\n",dbClassName);
        add("\t\tsuper(\"%s\");\n",dataBase.databaseName);

        for (Reference reference : dataBase.references) {
            if (reference instanceof ColumnReference) {
                ColumnReference ref = (ColumnReference) reference;
                String pkText = getColumnText(ref.primaryColumn);
                String fkText = getColumnText(ref.foreignColumn);
                add("\t\treferences.add(new ColumnReference(%s,%s,%s));\n",pkText,fkText,ref.isDirect);
            }
            if (reference instanceof CompositeColumnReference) {
                CompositeColumnReference ref = (CompositeColumnReference) reference;
                String pkText = getCompositeColumnText(ref.primaryCompositeColumn);
                String fkText = getCompositeColumnText(ref.foreignCompositeColumn);
                add("\t\treferences.add(new CompositeColumnReference(%s,%s));\n",pkText,fkText);
            }
        }
        add("\t}\n");
        add("}\n");


        File file = new File(path + File.separator + Utils.replacePackageToPath(dbPackage) + File.separator + dbClassName + ".java");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(getText().getBytes("UTF-8"));
        fileOutputStream.close();
    }



    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }
        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    private String getSchemaSection(DataBase dataBase, String dbPackage, String dbPath) throws IOException, GenerateException {
        StringBuilder sb = new StringBuilder();
        String schemaPackage = dbPackage + "." + Utils.toCamelCaseFirstLower(dataBase.databaseName);
        String schemaPath = dbPath + File.separator +Utils.toCamelCaseFirstLower(dataBase.databaseName);
        Utils.checkDir(schemaPath);
        for (Schema schema : dataBase.schemas) {
            SchemaGenerator schemaGenerator = new SchemaGenerator(this,schema,schemaPackage,schemaPath);
            schemaGenerator.save();
            sb.append(schemaGenerator.getVarText());
            classNames.add(schemaGenerator.getFullClassName());
        }
        return sb.toString();
    }

    private static String getColumnText(Column column) {
        String columnName = column.columnName;
        String tableName = Utils.toCamelCaseFirstLower(column.table.tableName);
        String schemaName = Utils.toCamelCaseFirstLower(column.table.schema.schemaName);
        return schemaName + "." + tableName + "." +columnName;
    }

    private String getCompositeColumnText(CompositeColumn primaryCompositeColumn) {
        String columnName = primaryCompositeColumn.getName();
        String tableName = Utils.toCamelCaseFirstLower(primaryCompositeColumn.table.tableName);
        String schemaName = Utils.toCamelCaseFirstLower(primaryCompositeColumn.table.schema.schemaName);
        return schemaName + "." + tableName + "." +columnName;
    }


}
