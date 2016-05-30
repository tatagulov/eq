package io.github.tatagulov.eq.test.mysql;

import io.github.tatagulov.eq.metadata.sql.MySQLDelete;
import io.github.tatagulov.eq.metadata.sql.ParamExpression;
import io.github.tatagulov.eq.metadata.sql.api.Delete;
import io.github.tatagulov.test.testData.entity.db.public_.Person;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMySQLDelete {
    public static final Integer TEST_INTEGER_VALUE = 1;

    @Test
    public void testDelete() throws Exception {
        Person person = new Person();
        person.where(person.person_id.eq(ParamExpression.param(TEST_INTEGER_VALUE)));
        Delete delete = new MySQLDelete(person);

        String sql = delete.getSQL();
        Object[] values = delete.getValues();

        assertEquals(sql, "delete from public.person where person_id = ?");
        assertEquals(values.length, 1);
        assertEquals(values[0],TEST_INTEGER_VALUE);

    }
}
