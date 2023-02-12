package org.acad;
import java.sql.Connection;
import DatabaseAccess.DBConnectionSingleton;
public class Main {
    public static void main(String[] args) {
        Connection connection = DBConnectionSingleton.getConnection();
        System.out.println(connection);
    }
}