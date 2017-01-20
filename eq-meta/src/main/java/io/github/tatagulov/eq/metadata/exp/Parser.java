package io.github.tatagulov.eq.metadata.exp;


import io.github.tatagulov.eq.metadata.api.Table;
import io.github.tatagulov.eq.metadata.exception.ObjectNotFoundException;
import io.github.tatagulov.eq.metadata.sql.SQLColumn;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private final static Pattern aggregatePattern = Pattern.compile(" *max\\(.+\\)| *min\\(.+\\)| *avg\\(.+\\)| *sum\\(.+\\)| *count\\(.+\\)");
    private final static Pattern columnPattern = Pattern.compile("\\{.[^\\}]+\\}");

    public static boolean isAggregate(String expression) {
        return aggregatePattern.matcher(expression).matches();
    }

    public static String parse(Table baseTable,String expression,List<SQLColumn> expressions) throws ObjectNotFoundException {
        Matcher matcher = columnPattern.matcher(expression);
        int index = 0;
        StringBuilder sql = new StringBuilder();
        while (matcher.find()) {
            String columnPath = expression.substring(matcher.start()+1,matcher.end()-1);
            String[] items = columnPath.split(":");
//
//            Column selectColumn = baseTable.getColumn(items[0]);
//            List<JoinExpression> joinExpressions = new LinkedList<>();
//            for (int i=1; i<items.length; i+=3) {
//                String linkColumnName = items[i];
//                String linkTableName = items[i+1];
//                String selectColumnName = items[i+2];
//                JoinType joinType = JoinType.inner;
//                if (linkColumnName.startsWith("+")) {
//                    joinType = JoinType.left;
//                    linkColumnName = linkColumnName.substring(1,linkColumnName.length());
//                }
//
//                Column linkColumn = selectColumn.getLinkColumn(linkTableName, linkColumnName);
//                JoinExpression joinExpression = new JoinExpression(joinType,selectColumn, linkColumn);
//                joinExpressions.add(joinExpression);
//                selectColumn = linkColumn.table.getColumn(selectColumnName);
//            }
//            SelectColumnExpression selectColumnExpression = new SelectColumnExpression(selectColumn);
//            selectColumnExpression.joinExpressions.addAll(joinExpressions);
//            expressions.add(selectColumnExpression);
//            sql.append(expression.substring(index, matcher.start()));
//            sql.append("%s");
//            index = matcher.end();
        }
        sql.append(expression.substring(index,expression.length()));
        return sql.toString();
    }

}
