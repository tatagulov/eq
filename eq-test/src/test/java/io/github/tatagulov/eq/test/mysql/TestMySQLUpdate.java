package io.github.tatagulov.eq.test.mysql;

import io.github.tatagulov.eq.metadata.sql.MySQLUpdate;
import io.github.tatagulov.eq.metadata.sql.ParamExpression;
import io.github.tatagulov.eq.metadata.sql.api.Update;
import io.github.tatagulov.test.testData.entity.db.public_.Person;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMySQLUpdate {
    public static final Integer TEST_INTEGER_VALUE = 1;
    public static final Integer TEST_INTEGER_VALUE2 = 1;

    public static final String LAST_NAME = "lastName";


    @Test
    public void testUpdate() throws Exception {
        Person person = new Person();
        person.where(person.person_id.eq(ParamExpression.param(TEST_INTEGER_VALUE2)));

        Update<Person> update = new MySQLUpdate<Person>(person);
        update.set(person.person_id,TEST_INTEGER_VALUE);
        update.set(person.last_name, LAST_NAME);

        String sql = update.getSQL();
        Object[] values = update.getValues();


        assertEquals(sql, "update public.person set person_id=?,last_name=? where person_id = ?");
        assertEquals(values.length, 3);
        assertEquals(values[0],TEST_INTEGER_VALUE);
        assertEquals(values[1],LAST_NAME);
        assertEquals(values[2],TEST_INTEGER_VALUE2);

    }
}
