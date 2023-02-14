package DatabaseAccess;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * This class is used to connect to the database.
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-12
 */
public class DBConnectionSingleton {
    final static String PROPERTIES_FILE = "database.properties";
    private static Connection connection;
    static Properties properties;
    static ClassLoader classLoader;
    static  {
        properties = new Properties();
        classLoader = DBConnectionSingleton.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(PROPERTIES_FILE);
        try {
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
}
