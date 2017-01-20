package io.github.tatagulov.eq.test;

import io.github.tatagulov.eq.metadata.exp.SQLExecutor;
import io.github.tatagulov.eq.metadata.exp.Setter;
import io.github.tatagulov.eq.metadata.exp.SimpleSQLExecutor;
import io.github.tatagulov.eq.metadata.sql.api.Select;
import io.github.tatagulov.test.testData.entity.db.public_.Person;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class TestSetter {

    class Data {
        public Integer person_id;
        public String person_name;
    }

    class MyExecutor implements SQLExecutor {

        @Override
        public ResultSet execute(Select select) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/eq-test","postgres","postgres");;
                try {
                    return new SimpleSQLExecutor(connection).execute(select);
                } finally {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final MyExecutor MY_EXECUTOR = new MyExecutor();

    @Test
    public void testSetter() throws Exception {

        final Person person = new Person();

        List<Data> datas = new Setter<Data>() {
            @Override
            protected Data map() {
                Data data = new Data();
                data.person_id = val(person.person_id);
                data.person_name = val(person.first_name);
                return data;
            }
        }.execute(MY_EXECUTOR);


        for (Data data : datas) {
            System.out.println("----------------");
            System.out.println(data.person_id);
            System.out.println(data.person_name);
        }
    }
}
