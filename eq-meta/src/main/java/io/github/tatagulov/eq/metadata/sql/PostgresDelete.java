package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.sql.api.Delete;

import java.util.LinkedList;
import java.util.List;

public class PostgresDelete implements Delete {

    protected SQLTable SQLTable;

    public PostgresDelete(SQLTable SQLTable) {
        this.SQLTable = SQLTable;
    }

    public String getSQL() {
        AliasGenerator aliasGenerator = new AliasGenerator("t");
        SQLTable.generateAlias(aliasGenerator);
        return "delete" + SQLTable.getFromSQL(aliasGenerator);
    }

    public Object[] getValues() {
        List<Object> values = new LinkedList<Object>();
        for (ParamExpression paramExpression : SQLTable.getParamExpressions()) {
            values.add(paramExpression.value);
        }
        return values.toArray(new Object[values.size()]);
    }
}
