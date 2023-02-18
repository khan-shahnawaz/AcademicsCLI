package database.access;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * This class is used to connect to the database.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-12
 */
public class DBConnectionSingleton {
    final static String PROPERTIES_FILE = "data/database.properties";
    static Properties properties;
    static ClassLoader classLoader;
    private static Connection connection;

    static {
        properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(PROPERTIES_FILE);
            properties.load(inputStream);
            String driver = properties.getProperty("JDBC_DRIVER");
            String url = properties.getProperty("URL");
            String username = properties.getProperty("USER");
            String password = properties.getProperty("PASSWORD");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static Connection restartConnection() {
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE));
            connection = DriverManager.getConnection(properties.getProperty("URL"), properties.getProperty("USER"), properties.getProperty("PASSWORD"));
            return connection;
        } catch (Exception e) {
            return null;
        }
    }
}
