package io.github.tatagulov.eq.metadata.sql;

import io.github.tatagulov.eq.metadata.sql.api.Delete;

import java.util.LinkedList;
import java.util.List;

public class MySQLDelete implements Delete {

    protected SQLTable sqlTable;

    public MySQLDelete(SQLTable sqlTable) {
        this.sqlTable = sqlTable;
    }

    public String getSQL() {
        AliasGenerator aliasGenerator = new AliasGenerator("t");
        return "delete" + sqlTable.getFromSQL(aliasGenerator);
    }

    public Object[] getValues() {
        List<Object> values = new LinkedList<Object>();
        for (ParamExpression paramExpression : sqlTable.getParamExpressions()) {
            values.add(paramExpression.value);
        }
        return values.toArray(new Object[values.size()]);
    }
}
