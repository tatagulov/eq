package io.github.tatagulov.eq.test.postgres;

import io.github.tatagulov.eq.metadata.sql.PostgresDelete;
import io.github.tatagulov.eq.metadata.sql.PostgresSelect;
import io.github.tatagulov.eq.metadata.sql.api.Select;
import io.github.tatagulov.test.testData.entity.db.public_.Person;
import io.github.tatagulov.test.testData.entity.db.public_.PersonMove;
import org.junit.Test;

import static io.github.tatagulov.eq.metadata.sql.ParamExpression.param;
import static org.junit.Assert.assertEquals;

public class TestPostgresDelete {

    public static final Short TEST_SHORT_VALUE = 1;

    @Test
    public void testSimpleDelete() throws Exception {
        Person person = new Person();
        person.where(person.person_type_id.eq(param(TEST_SHORT_VALUE)));

        PostgresDelete delete = new PostgresDelete(person);
        String sql = delete.getSQL();
        Object[] values = delete.getValues();
        assertEquals(sql, "delete from public.person t0 where t0.person_type_id = ?");
        assertEquals(values.length, 1);
        assertEquals(values[0],TEST_SHORT_VALUE);
    }

    @Test
    public void testDeleteSubSelect() throws Exception {
        PersonMove personMove = new PersonMove();
        personMove.where(personMove.to_dep_id.eq(param(TEST_SHORT_VALUE)));

        Select select = new PostgresSelect();
        select.select(personMove.person_id);

        Person person = new Person();
        person.where(person.person_id.in(select.asExpression()));

        PostgresDelete delete = new PostgresDelete(person);

        String sql = delete.getSQL();
        Object[] values = delete.getValues();
        assertEquals(sql, "delete from public.person t0 where t0.person_id in ((select t1.person_id from public.person_move t1 where t1.to_dep_id = ?))");
        assertEquals(values.length, 1);
        assertEquals(values[0], TEST_SHORT_VALUE);
    }
}
