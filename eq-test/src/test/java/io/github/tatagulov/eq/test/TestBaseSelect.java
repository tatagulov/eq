package io.github.tatagulov.eq.test;

import io.github.tatagulov.eq.metadata.sql.*;
import io.github.tatagulov.eq.metadata.sql.api.Select;
import io.github.tatagulov.eq.metadata.sql.TwoCondition;
import io.github.tatagulov.test.testData.entity.db.public_.Person;
import io.github.tatagulov.test.testData.entity.db.public_.PersonMove;
import io.github.tatagulov.test.testData.entity.db.public_.PersonType;
import org.junit.Test;

import java.sql.Date;

import static io.github.tatagulov.eq.metadata.sql.ParamExpression.param;
import static io.github.tatagulov.eq.metadata.sql.SQLFunction.*;
import static io.github.tatagulov.eq.metadata.sql.ValueExpression.value;
import static org.junit.Assert.assertEquals;

public class TestBaseSelect {

    public static final Integer TEST_INT_VALUE = 1;
    public static final String TEST_STRING_VALUE = "testValue";
    public static final String TEST_STRING_VALUE2 = "testValue2";
    public static final Short TEST_SHORT_VALUE = 1;
    public static final Short TEST_SHORT_VALUE2 = 2;

    public static final Long TEST_LONG_VALUE = (long) 1;

    private BaseSelect createSelect() {
        return new BaseSelect();
    }

    @Test
    public void testSelectFormDual() throws Exception {
        Select select = createSelect();
        select.select(value(1));

        String sql = select.getSQL();
        Object[] values = select.getValues();
        assertEquals(sql, "values(1)");
        assertEquals(values.length, 0);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testSimpleSelect() {
        Person person = new Person();

        Select select = createSelect();
        select.select(person.person_id);
        select.select(person.person_type_id);

        String sql = select.getSQL();
        Object[] values = select.getValues();
        assertEquals(sql, "select t0.person_id,t0.person_type_id from public.person t0");
        assertEquals(values.length, 0);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testJoin() {
        Person person = new Person();
        PersonType personType = person.person_type_id.innerPersonType();

        Select select = createSelect();
        select.select(person.person_id);
        select.select(personType.person_type_name);

        String sql = select.getSQL();
        assertEquals(sql, "select t0.person_id,t1.person_type_name from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id");

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testJoinInline() {
        Person person = new Person();

        Select select = createSelect();
        select.select(person.person_id);
        select.select(person.person_type_id.innerPersonType().person_type_name);

        String sql = select.getSQL();
        assertEquals(sql, "select t0.person_id,t1.person_type_name from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id");

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testJoinOptimize() {
        Person person = new Person();
        PersonType personType = person.person_type_id.innerPersonType();

        Select select = createSelect();
        select.select(person.person_id);
        select.select(personType.person_type_name);
        testJoinOptimizeAssert(select,"select t0.person_id,t1.person_type_name from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id");

        select.select(personType.person_type_id);
        testJoinOptimizeAssert(select,"select t0.person_id,t1.person_type_name,t1.person_type_id from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id");

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id");
        assertEquals(countValues.length, 0);
    }

    private void testJoinOptimizeAssert(Select select,String assertSql) {
        String sql = select.getSQL();
        assertEquals(sql, assertSql);
    }

    @Test
    public void testWhere() throws Exception {
        Person person = new Person();
        person.where(person.person_id.eq(param(TEST_INT_VALUE)));

        Select select = createSelect();
        select.select(person.last_name);

        String sql = select.getSQL();
        Object[] values = select.getValues();
        assertEquals(sql, "select t0.last_name from public.person t0 where t0.person_id = ?");
        assertEquals(values.length, 1);
        assertEquals(values[0], TEST_INT_VALUE);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0 where t0.person_id = ?");
        assertEquals(countValues.length, 1);
        assertEquals(countValues[0], TEST_INT_VALUE);
    }

    @Test
    public void testJoinWhere() {
        Person person = new Person();
        PersonType personType = person.person_type_id.innerPersonType();
        personType.where(personType.person_type_name.eq(param(TEST_STRING_VALUE)));

        Select select = createSelect();
        select.select(person.person_id);

        String sql = select.getSQL();
        Object[] values = select.getValues();
        assertEquals(sql, "select t0.person_id from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id and t1.person_type_name = ?");
        assertEquals(values.length, 1);
        assertEquals(values[0], TEST_STRING_VALUE);


        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id and t1.person_type_name = ?");
        assertEquals(countValues.length, 1);
        assertEquals(countValues[0], TEST_STRING_VALUE);

    }

    @Test
    public void testJoinManyWhere() {
        Person person = new Person();
        person.where(person.person_id.eq(param(TEST_INT_VALUE)));

        PersonType personType = person.person_type_id.innerPersonType();
        personType.where(personType.person_type_name.eq(param(TEST_STRING_VALUE)).or(personType.person_type_name.eq(param(TEST_STRING_VALUE2))));
        personType.where(personType.person_type_id.eq(param(TEST_SHORT_VALUE)).or(personType.person_type_id.eq(param(TEST_SHORT_VALUE2))));

        Select select = createSelect();
        select.select(person.person_id);
        select.select(person.first_name);

        String sql = select.getSQL();
        Object[] values = select.getValues();

        assertEquals(sql, "select t0.person_id,t0.first_name from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id and (t1.person_type_name = ? or t1.person_type_name = ?) and (t1.person_type_id = ? or t1.person_type_id = ?) where t0.person_id = ?");
        assertEquals(values.length, 5);
        assertEquals(values[0], TEST_STRING_VALUE);
        assertEquals(values[1], TEST_STRING_VALUE2);
        assertEquals(values[2], TEST_SHORT_VALUE);
        assertEquals(values[3], TEST_SHORT_VALUE2);
        assertEquals(values[4], TEST_INT_VALUE);


        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id and (t1.person_type_name = ? or t1.person_type_name = ?) and (t1.person_type_id = ? or t1.person_type_id = ?) where t0.person_id = ?");
        assertEquals(countValues.length, 5);
        assertEquals(countValues[0], TEST_STRING_VALUE);
        assertEquals(countValues[1], TEST_STRING_VALUE2);
        assertEquals(countValues[2], TEST_SHORT_VALUE);
        assertEquals(countValues[3], TEST_SHORT_VALUE2);
        assertEquals(countValues[4], TEST_INT_VALUE);

    }

    @Test
    public void testJoinAliasTable() {
        PersonMove personMove = new PersonMove();
        Select select = createSelect();
        select.select(personMove.from_dep_id.innerDep().dep_name.as("from_dep"));
        select.select(personMove.to_dep_id.innerDep().dep_name.as("to_dep"));

        String sql = select.getSQL();
        Object[] values = select.getValues();
        assertEquals(sql, "select t1.dep_name as from_dep,t2.dep_name as to_dep from public.person_move t0 inner join public.dep t1 on t0.from_dep_id = t1.dep_id inner join public.dep t2 on t0.to_dep_id = t2.dep_id");
        assertEquals(values.length, 0);


        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person_move t0 inner join public.dep t1 on t0.from_dep_id = t1.dep_id inner join public.dep t2 on t0.to_dep_id = t2.dep_id");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testAggregate() throws Exception {
        PersonType personType = new PersonType();

        Select select = createSelect();
        select.select(count(personType.person_type_id).as("cnt"));

        String sql = select.getSQL();
        assertEquals(sql, "select count(t0.person_type_id) as cnt from public.person_type t0");

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from (select count(t0.person_type_id) as cnt from public.person_type t0)");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testAggregateWithGroupBy() throws Exception {
        Person person = new Person();

        Select select = createSelect();
        select.select(person.person_type_id);
        select.select(count(person.person_id).as("cnt"));

        String sql = select.getSQL();
        assertEquals(sql, "select t0.person_type_id,count(t0.person_id) as cnt from public.person t0 group by t0.person_type_id");

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from (select t0.person_type_id,count(t0.person_id) as cnt from public.person t0 group by t0.person_type_id)");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testComplexGroupBy() throws Exception {
        PersonType personType = new PersonType();
        Person person = personType.person_type_id.innerPerson();

        Select select = createSelect();
        select.select(concat(personType.person_type_name, value("("), count(person.person_id), value(")")).as("text"));
        select.select(personType.person_type_id);

        String sql = select.getSQL();
        assertEquals(sql, "select concat(t0.person_type_name,'(',count(t1.person_id),')') as text,t0.person_type_id from public.person_type t0 inner join public.person t1 on t0.person_type_id = t1.person_type_id group by t0.person_type_name,t0.person_type_id");

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from (select concat(t0.person_type_name,'(',count(t1.person_id),')') as text,t0.person_type_id from public.person_type t0 inner join public.person t1 on t0.person_type_id = t1.person_type_id group by t0.person_type_name,t0.person_type_id)");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testLimit() throws Exception {
        Person person = new Person();

        Select select = createSelect();
        select.select(person.person_id);
        select.select(person.first_name);

        select.limit(10l);
        select.offset(20l);

        String sql = select.getSQL();
        assertEquals(sql, "select t0.person_id,t0.first_name from public.person t0 limit 10 offset 20");

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testOrderBy() throws Exception {
        Person person = new Person();

        Select select = createSelect();
        select.select(person.person_id);
        select.select(person.first_name);

        select.orderBy(person.person_id);
        select.orderBy(person.first_name);

        String sql = select.getSQL();
        assertEquals(sql, "select t0.person_id,t0.first_name from public.person t0 order by t0.person_id asc,t0.first_name asc");

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testSelectAndWhere() throws Exception {
        Person person = new Person();
        PersonMove personMove = person.person_id.innerPersonMove();

        person.where(person.person_id.eq(param(TEST_INT_VALUE)));

        Select select = createSelect();
        select.select(personMove.person_move_id);

        String sql = select.getSQL();
        Object[] values = select.getValues();

        assertEquals(sql, "select t1.person_move_id from public.person t0 inner join public.person_move t1 on t0.person_id = t1.person_id where t0.person_id = ?");
        assertEquals(values.length, 1);
        assertEquals(values[0], TEST_INT_VALUE);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0 inner join public.person_move t1 on t0.person_id = t1.person_id where t0.person_id = ?");
        assertEquals(countValues.length, 1);
        assertEquals(countValues[0], TEST_INT_VALUE);

    }

    @Test
    public void testCaseWhen() throws Exception {
        Person person = new Person();
        Select select = createSelect();
        select.select(when(person.first_name.eq(param(TEST_STRING_VALUE)), value(1)).otherwise(value(0)).as("test"));

        String sql = select.getSQL();
        Object[] values = select.getValues();
        assertEquals(sql, "select case when t0.first_name = ? then 1 else 0 end as test from public.person t0");
        assertEquals(values.length, 1);
        assertEquals(values[0], TEST_STRING_VALUE);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testHaving() throws Exception {

        Person person = new Person();

        Select select = createSelect();
        select.select(person.person_type_id);

        select.having(TwoCondition.eq(count(person.person_id), param(TEST_LONG_VALUE)));
        select.having(TwoCondition.eq(max(person.person_id), value(TEST_INT_VALUE)));
        select.having(TwoCondition.eq(max(person.person_id), count(person.person_id)));

        String sql = select.getSQL();
        Object[] values = select.getValues();
        assertEquals(sql, "select t0.person_type_id from public.person t0 group by t0.person_type_id having count(t0.person_id) = ? and max(t0.person_id) = 1 and max(t0.person_id) = count(t0.person_id)");
        assertEquals(values.length, 1);
        assertEquals(values[0], TEST_LONG_VALUE);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from (select t0.person_type_id from public.person t0 group by t0.person_type_id having count(t0.person_id) = ? and max(t0.person_id) = 1 and max(t0.person_id) = count(t0.person_id))");
        assertEquals(countValues.length, 1);
        assertEquals(countValues[0], TEST_LONG_VALUE);

    }

    @Test
    public void testSubSelect() throws Exception {
        final PersonMove personMove = new PersonMove();

        BaseSelect subSelect = createSelect();

        AliasColumn<? extends Select, Integer> personId = subSelect.select(personMove.person_id);
        AliasColumn<? extends Select, Long> personCnt = subSelect.select(count(personMove.person_id).as("cnt"));

        Person person = new Person();
        person.join(JoinType.inner, person.person_id, personId);

        Select select = createSelect();
        select.select(person.first_name);
        select.select(person.last_name);
        select.select(personCnt);

        String sql = select.getSQL();
        Object[] values = select.getValues();

        assertEquals(sql, "select t0.first_name,t0.last_name,t1.cnt from public.person t0 inner join (select t2.person_id,count(t2.person_id) as cnt from public.person_move t2 group by t2.person_id) t1 on t0.person_id = t1.person_id");
        assertEquals(values.length, 0);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0 inner join (select t2.person_id,count(t2.person_id) as cnt from public.person_move t2 group by t2.person_id) t1 on t0.person_id = t1.person_id");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testSubSelectUseClass() throws Exception {

        final PersonMove personMove = new PersonMove();

        class MyView extends PostgresSelect {
            AliasColumn<MyView,Integer> personId = new AliasColumn<MyView,Integer>(this,personMove.person_id);
            AliasColumn<MyView,Long> personCnt = new AliasColumn<MyView,Long>(this, count(personMove.person_id).as("cnt"));
        }

        Person person = new Person();
        MyView myView = person.join(JoinType.inner, person.person_id, new MyView().personId);

        Select select = createSelect();
        select.select(person.first_name);
        select.select(person.last_name);
        select.select(myView.personCnt);

        String sql = select.getSQL();
        Object[] values = select.getValues();

        assertEquals(sql, "select t0.first_name,t0.last_name,t1.cnt from public.person t0 inner join (select t2.person_id,count(t2.person_id) as cnt from public.person_move t2 group by t2.person_id) t1 on t0.person_id = t1.person_id");
        assertEquals(values.length, 0);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0 inner join (select t2.person_id,count(t2.person_id) as cnt from public.person_move t2 group by t2.person_id) t1 on t0.person_id = t1.person_id");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testSubSelectInWhere() throws Exception {
        PersonMove personMove = new PersonMove();
        personMove.where(personMove.from_dep_id.eq(param(TEST_SHORT_VALUE)));
        personMove.where(personMove.to_dep_id.eq(param(TEST_SHORT_VALUE2)));

        BaseSelect subSelect = createSelect();
        subSelect.select(personMove.person_id);

        Person person = new Person();
        person.where(person.first_name.eq(param(TEST_STRING_VALUE)));
        person.where(person.person_id.in(subSelect.asExpression()));

        Select select = createSelect();
        select.select(person.person_id);
        select.select(person.last_name);

        String sql = select.getSQL();
        Object[] values = select.getValues();

        assertEquals(sql, "select t0.person_id,t0.last_name from public.person t0 where t0.first_name = ? and t0.person_id in (select t1.person_id from public.person_move t1 where t1.from_dep_id = ? and t1.to_dep_id = ?)");
        assertEquals(values.length, 3);
        assertEquals(values[0], TEST_STRING_VALUE);
        assertEquals(values[1], TEST_SHORT_VALUE);
        assertEquals(values[2], TEST_SHORT_VALUE2);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0 where t0.first_name = ? and t0.person_id in (select t1.person_id from public.person_move t1 where t1.from_dep_id = ? and t1.to_dep_id = ?)");
        assertEquals(values.length, 3);
        assertEquals(countValues[0], TEST_STRING_VALUE);
        assertEquals(countValues[1], TEST_SHORT_VALUE);
        assertEquals(countValues[2], TEST_SHORT_VALUE2);
    }

    @Test
    public void testSubSelectInSelect() throws Exception {
        Person person = new Person();

        PersonMove personMove = new PersonMove();
        personMove.where(personMove.person_id.eq(person.person_id));

        Select subBaseSelect = createSelect();
        subBaseSelect.select(max(personMove.move_date));

        Select select = createSelect();
        select.select(person.person_id);
        select.select(subBaseSelect.asAliasExpression("max_move_date"));

        String sql = select.getSQL();
        Object[] values = select.getValues();

        assertEquals(sql, "select t0.person_id,(select max(t1.move_date) from public.person_move t1 where t1.person_id = t0.person_id) as max_move_date from public.person t0");
        assertEquals(values.length, 0);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0");
        assertEquals(countValues.length, 0);
    }

    @Test
    public void testUnion() {
        Person person1 = new Person();
        person1.where(person1.person_type_id.eq(param(TEST_SHORT_VALUE)));

        Select select = createSelect();
        select.select(person1.first_name);
        select.select(person1.last_name);

        Person person2 = new Person();
        person2.where(person2.person_type_id.eq(param(TEST_SHORT_VALUE2)));

        BaseSelect select2 = createSelect();
        select2.select(person2.first_name);
        select2.select(person2.last_name);

        select.unionAll(select2);

        String sql = select.getSQL();
        Object[] values = select.getValues();

        assertEquals(sql, "select t0.first_name,t0.last_name from public.person t0 where t0.person_type_id = ? union all select t1.first_name,t1.last_name from public.person t1 where t1.person_type_id = ?");
        assertEquals(values.length, 2);
        assertEquals(values[0], TEST_SHORT_VALUE);
        assertEquals(values[1], TEST_SHORT_VALUE2);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select sum(cnt) from (select count(*) as cnt from public.person t0 where t0.person_type_id = ? union all select count(*) as cnt from public.person t1 where t1.person_type_id = ?) as foo");
        assertEquals(countValues.length, 2);
        assertEquals(countValues[0], TEST_SHORT_VALUE);
        assertEquals(countValues[1], TEST_SHORT_VALUE2);
    }

    @Test
    public void testUnionInSubSelect() throws Exception {
        Person person1 = new Person();
        person1.where(person1.person_type_id.eq(param(TEST_SHORT_VALUE)));

        Select baseSelect1 = createSelect();
        baseSelect1.select(person1.person_id);


        Person person2 = new Person();
        person2.where(person2.person_type_id.eq(param(TEST_SHORT_VALUE2)));

        BaseSelect baseSelect2 = createSelect();
        baseSelect2.select(person2.person_id);

        baseSelect1.unionAll(baseSelect2);

        PersonMove personMove = new PersonMove();
        personMove.where(personMove.person_id.in(baseSelect1));

        Select select = createSelect();
        select.select(personMove.person_id);
        select.select(personMove.from_dep_id);

        testUnionInSubSelectAssert(select);
        testUnionInSubSelectAssert(select);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person_move t0 where t0.person_id in (select t1.person_id from public.person t1 where t1.person_type_id = ? union all select t2.person_id from public.person t2 where t2.person_type_id = ?)");
        assertEquals(countValues.length, 2);
        assertEquals(countValues[0], TEST_SHORT_VALUE);
        assertEquals(countValues[1], TEST_SHORT_VALUE2);
    }

    private void testUnionInSubSelectAssert(Select select) {
        String sql = select.getSQL();
        Object[] values = select.getValues();

        assertEquals(sql, "select t0.person_id,t0.from_dep_id from public.person_move t0 where t0.person_id in (select t1.person_id from public.person t1 where t1.person_type_id = ? union all select t2.person_id from public.person t2 where t2.person_type_id = ?)");
        assertEquals(values.length, 2);
        assertEquals(values[0], TEST_SHORT_VALUE);
        assertEquals(values[1], TEST_SHORT_VALUE2);
    }


    @Test
    public void testOrderByJoin() throws Exception {
        Person person = new Person();

        Select select = new PostgresSelect();
        select.select(person.last_name);

        select.orderBy(person.person_type_id.innerPersonType().person_type_name);

        String sql = select.getSQL();
        Object[] values = select.getValues();

        assertEquals(sql,"select t0.last_name from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id order by t1.person_type_name asc");
        assertEquals(values.length, 0);

        String countSQL = select.getCountSQL();
        Object[] countValues = select.getCountValues();
        assertEquals(countSQL, "select count(*) as cnt from public.person t0 inner join public.person_type t1 on t0.person_type_id = t1.person_type_id");
        assertEquals(countValues.length, 0);
    }


}
