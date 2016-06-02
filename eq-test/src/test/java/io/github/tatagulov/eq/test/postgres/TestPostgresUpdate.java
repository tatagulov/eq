package io.github.tatagulov.eq.test.postgres;

import io.github.tatagulov.eq.metadata.sql.SQLFunction;
import io.github.tatagulov.eq.metadata.sql.PostgresUpdate;
import io.github.tatagulov.eq.metadata.sql.api.Update;
import io.github.tatagulov.test.testData.entity.db.public_.Person;
import org.junit.Test;

import java.sql.Date;

import static io.github.tatagulov.eq.metadata.sql.ParamExpression.param;
import static io.github.tatagulov.eq.metadata.sql.ValueExpression.value;
import static org.junit.Assert.assertEquals;

public class TestPostgresUpdate {

    public static final Integer TEST_INT_VALUE = 1;
    public static final Integer TEST_INT_VALUE2 = 2;
    public static final String TEST_STRING_VALUE = "testValue";
    public static final String TEST_STRING_VALUE2 = "testValue2";
    public static final Short TEST_SHORT_VALUE = 1;
    public static final Date TEST_DATE_VALUE1 = new Date(100000);

    @Test
    public void testSimpleUpdate() throws Exception {
        Person person = new Person();
        person.where(person.person_id.eq(param(TEST_INT_VALUE2)));

        Update<Person> update = new PostgresUpdate<Person>(person);
        update.set(person.person_id, TEST_INT_VALUE);
        update.set(person.first_name, TEST_STRING_VALUE);
        update.set(person.last_name, TEST_STRING_VALUE2);
        update.set(person.birth_date, TEST_DATE_VALUE1);
        update.set(person.person_type_id, TEST_SHORT_VALUE);

        String sql = update.getSQL();
        Object[] values = update.getValues();

        assertEquals(sql, "update public.person as t0 set (person_id,first_name,last_name,birth_date,person_type_id) = (?,?,?,?,?) where t0.person_id = ?");
        assertEquals(values.length, 6);
        assertEquals(values[0], TEST_INT_VALUE);
        assertEquals(values[1], TEST_STRING_VALUE);
        assertEquals(values[2], TEST_STRING_VALUE2);
        assertEquals(values[3], TEST_DATE_VALUE1);
        assertEquals(values[4], TEST_SHORT_VALUE);
        assertEquals(values[5], TEST_INT_VALUE2);
    }

    @Test
    public void testUpdateOldValue() throws Exception {
        Person person = new Person();
        person.where(person.person_id.eq(param(TEST_INT_VALUE)));

        Update<Person> update = new PostgresUpdate<Person>(person);
        update.set(person.person_type_id, SQLFunction.plus(person.person_type_id, value(TEST_SHORT_VALUE)));

        String sql = update.getSQL();
        Object[] values = update.getValues();

        assertEquals(sql, "update public.person as t0 set (person_type_id) = (t0.person_type_id + 1) where t0.person_id = ?");
        assertEquals(values.length, 1);
        assertEquals(values[0], TEST_INT_VALUE);
    }

    @Test
    public void testUpdateFromSelect() throws Exception {
        Person person = new Person();
        person.where(person.person_id.eq(param(TEST_INT_VALUE)));

        Person person1 = new Person();
        person1.where(person1.person_id.eq(person.person_id));

        Update<Person> update = new PostgresUpdate<Person>(person);
        update.set(person.person_type_id, SQLFunction.plus(person1.person_type_id,person.person_type_id));
        update.set(person.first_name, person1.first_name);

        String sql = update.getSQL();
        Object[] values = update.getValues();

        assertEquals(sql, "update public.person as t0 set (person_type_id,first_name) = (t1.person_type_id + t0.person_type_id,t1.first_name) from public.person t1 where t1.person_id = t0.person_id and t0.person_id = ?");
        assertEquals(values.length, 1);
        assertEquals(values[0], TEST_INT_VALUE);
    }
}