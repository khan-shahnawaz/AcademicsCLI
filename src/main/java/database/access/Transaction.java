package database.access;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction {
    static Connection connection;

    static {
        connection = DBConnectionSingleton.getConnection();
    }

    public static boolean start() {
        try {
            connection.setAutoCommit(false);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean commit() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean rollback() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
