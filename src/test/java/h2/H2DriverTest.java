package h2;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class H2DriverTest {
    @Test
    void test1() throws Exception {
        Class.forName("org.h2.Driver");
        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "scott", "tiger")) {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("select * from test");
            System.out.println(resultSet.getMetaData().getColumnName(1) + " " + resultSet.getMetaData().getColumnName(2));
            while (resultSet.next()) {
                System.out.println(resultSet.getObject(1) + " " + resultSet.getObject(2));
            }
        }
    }
}
