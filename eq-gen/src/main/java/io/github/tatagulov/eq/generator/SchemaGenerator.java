package io.github.tatagulov.eq.generator;

import io.github.tatagulov.eq.generator.exception.GenerateException;
import io.github.tatagulov.eq.metadata.api.DataBase;
import io.github.tatagulov.eq.metadata.api.Schema;
import io.github.tatagulov.eq.metadata.api.Sequence;
import io.github.tatagulov.eq.metadata.api.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SchemaGenerator extends Template {

    private final String schemaClassName;
    private final DataBaseGenerator dataBaseGenerator;
    private final Schema schema;
    private final String schemaPackage;
    private final String schemaPath;


    public SchemaGenerator(DataBaseGenerator dataBaseGenerator, Schema schema, String schemaPackage, String schemaPath) throws GenerateException, IOException {
        this.dataBaseGenerator = dataBaseGenerator;
        this.schema = schema;
        this.schemaPackage = schemaPackage;
        this.schemaPath = schemaPath;
        this.schemaClassName = Utils.toCamelCase(schema.schemaName);

        getClassName(Sequence.class);
        String schemaClass = getClassName(Schema.class);
        String tableSection = getTableSection();
        String dataBaseClass = getClassName(DataBase.class);
        String importSection = getImportSection();

        add("package %s;\n\n",schemaPackage);
        add(importSection);
        add("public class %s extends %s {\n", schemaClassName,schemaClass);
        add("\tpublic %s(%s dataBase) { super(dataBase, \"%s\");}\n", schemaClassName,dataBaseClass,schema.schemaName);
        add(tableSection);
        add("}\n");
    }

    protected String getTableSection() throws GenerateException, IOException {
        StringBuilder sb = new StringBuilder();
        String tablePackage = schemaPackage + "." + Utils.toCamelCaseFirstLower(schema.schemaName);
        String tablePath = schemaPath + File.separator + Utils.toCamelCaseFirstLower(schema.schemaName);
        Utils.checkDir(tablePath);
        for (Table table : schema.tables) {
            MDTableGenerator mdTableGenerator = new MDTableGenerator(dataBaseGenerator,table,tablePackage,tablePath);
            mdTableGenerator.save();
            sb.append(mdTableGenerator.getVarText());
            classNames.add(mdTableGenerator.getFullClassName());

            TableGenerator tableGenerator = new TableGenerator(dataBaseGenerator,table,tablePackage,tablePath);
            tableGenerator.save();
        }
        sb.append("\n");
        for (Sequence sequence : schema.sequences) {
            String varName = sequence.sequenceName.toLowerCase();
            sb.append(String.format("\tpublic final Sequence %s = new Sequence(this,\"%s\");\n",varName,sequence.sequenceName));
        }
        return sb.toString();
    }

    public String getVarText() {
        String varName = Utils.toCamelCaseFirstLower(schema.schemaName);
        return String.format("\tpublic final %s %s = new %s(this);\n",schemaClassName,varName,schemaClassName);
    }

    public void save() throws IOException {
        File file = new File(schemaPath + File.separator + schemaClassName + ".java");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(getText().getBytes("UTF-8"));
        fileOutputStream.close();
    }

    public String getFullClassName() {
        return schemaPackage + "." + schemaClassName;
    }
}
