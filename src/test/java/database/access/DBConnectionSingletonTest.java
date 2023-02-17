package database.access;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

class DBConnectionSingletonTest {

    @Test
    void getConnection() {
        Connection connection = DBConnectionSingleton.getConnection();
        Assertions.assertNotNull(connection);
    }
}