package io.github.tatagulov.eq.generator;

import io.github.tatagulov.eq.generator.exception.GenerateException;
import io.github.tatagulov.eq.metadata.api.Column;
import io.github.tatagulov.eq.metadata.api.CompositeColumn;


public class MDCompositeColumnGenerator extends Template {

    private final CompositeColumn compositeColumn;
    private final String tableClassName;
    private final String columnClassName;

    public MDCompositeColumnGenerator(CompositeColumn<?> compositeColumn, String tableClassName) throws GenerateException {
        this.compositeColumn = compositeColumn;
        this.tableClassName = tableClassName;
        columnClassName = getClassName(CompositeColumn.class);
    }

    public String getVarText() {
        String varName = compositeColumn.getName();
        StringBuilder columnNames = new StringBuilder();


        for (Column column : compositeColumn.columns) {
            if (columnNames.length()>0) columnNames.append(",");
            String columnVarName = column.columnName;
            columnNames.append(columnVarName);
        }

        return String.format("\tpublic final %s<%s> %s = new %s<%s>(this,%s);\n",
                columnClassName,
                tableClassName,
                varName,
                columnClassName,
                tableClassName,
                columnNames);
    }
}
