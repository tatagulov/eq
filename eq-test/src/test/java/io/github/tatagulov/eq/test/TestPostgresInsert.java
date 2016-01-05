package io.github.tatagulov.eq.test;

import io.github.tatagulov.eq.metadata.sql.api.Insert;
import io.github.tatagulov.eq.metadata.sql.PostgresInsert;
import io.github.tatagulov.test.testData.entity.db.public_.Person;
import io.github.tatagulov.test.testData.entity.db.public_.PersonMove;
import org.junit.Test;

import java.sql.Date;

import static io.github.tatagulov.eq.metadata.sql.ValueExpression.value;
import static org.junit.Assert.assertEquals;

public class TestPostgresInsert {

    public static final Integer TEST_INT_VALUE = 1;
    public static final String TEST_STRING_VALUE = "testValue";
    public static final String TEST_STRING_VALUE2 = "testValue2";
    public static final Short TEST_SHORT_VALUE = 1;
    public static final Short TEST_SHORT_VALUE2 = 2;
    public static final Date TEST_DATE_VALUE1 = new Date(100000);

    @Test
    public void testSimpleInsert() throws Exception {
        Person person = new Person();
        Insert<Person> insert = new PostgresInsert<Person>(person);
        insert.set(person.person_id,TEST_INT_VALUE);
        insert.set(person.first_name,TEST_STRING_VALUE);
        insert.set(person.last_name,TEST_STRING_VALUE2);
        insert.set(person.birth_date,TEST_DATE_VALUE1);
        insert.set(person.person_type_id, value(TEST_SHORT_VALUE));

        String sql = insert.getSQL();
        Object[] values = insert.getValues();

        assertEquals(sql, "insert into public.person(person_id,first_name,last_name,birth_date,person_type_id) values(?,?,?,?,1)");
        assertEquals(values.length, 4);
        assertEquals(values[0], TEST_INT_VALUE);
        assertEquals(values[1], TEST_STRING_VALUE);
        assertEquals(values[2], TEST_STRING_VALUE2);
        assertEquals(values[3], TEST_DATE_VALUE1);
    }

    @Test
    public void testInsertFromSelect() throws Exception {
        Person person = new Person();

        PersonMove personMove = new PersonMove();
        Insert<PersonMove> insert = new PostgresInsert<PersonMove>(personMove);
        insert.set(personMove.person_id,person.person_id);
        insert.set(personMove.from_dep_id,TEST_SHORT_VALUE);
        insert.set(personMove.to_dep_id,TEST_SHORT_VALUE2);
        insert.set(personMove.person_move_id,TEST_INT_VALUE);
        insert.set(personMove.move_date,TEST_DATE_VALUE1);

        insert.limit(10l);

        String sql = insert.getSQL();
        Object[] values = insert.getValues();

        assertEquals(sql, "insert into public.person_move(person_id,from_dep_id,to_dep_id,person_move_id,move_date) select t0.person_id,?,?,?,? from public.person t0 limit 10");
        assertEquals(values.length, 4);
        assertEquals(values[0], TEST_SHORT_VALUE);
        assertEquals(values[1], TEST_SHORT_VALUE2);
        assertEquals(values[2], TEST_INT_VALUE);
        assertEquals(values[3], TEST_DATE_VALUE1);
    }
}
