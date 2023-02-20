package org.acad;

import database.access.DBConnectionSingleton;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Class to deal with subcommand config.
 *
 * @author Shahnawaz Khan
 * @version 1.0
 * @since 2023-02-18
 */

@Command(name = "config", mixinStandardHelpOptions = true, description = "Configure the database connection and login credentials.")
public class Configuration implements Callable<Integer> {
    private static final String PROPERTIES_FILE = String.join(System.getProperty("file.separator"), System.getProperty("user.home"), ".academic", "database.properties");
    public static int SUCCESS = 0;
    @Option(names = {"-h", "--host"}, description = "The host name of the database server.")
    private String host;
    @Option(names = {"-p", "--port"}, description = "The port number of the database server.")
    private String port;
    @Option(names = {"-d", "--database"}, description = "The name of the database.")
    private String database;
    @Option(names = {"-u", "--user"}, description = "The username to connect to the database.")
    private String user;
    @Option(names = {"-w", "--password"}, description = "The password to connect to the database.", interactive = true, prompt = "Password: ")
    private String password;

    public Integer call() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream(PROPERTIES_FILE);
            properties.load(inputStream);
            inputStream.close();
            String url = properties.getProperty("URL");
            String[] urlParts = url.split(":");
            String prevHost = urlParts[2].substring(2);
            String prevPort = urlParts[3].split("/")[0];
            String prevDatabase = urlParts[3].split("/")[1];
            String prevUser = properties.getProperty("USER");
            String prevPassword = properties.getProperty("PASSWORD");
            if (host == null) {
                host = prevHost;
            }
            if (port == null) {
                port = prevPort;
            }
            if (database == null) {
                database = prevDatabase;
            }
            if (user == null) {
                user = prevUser;
            }
            if (password == null) {
                password = prevPassword;
            }
            url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            properties.clear();
            properties.setProperty("URL", url);
            properties.setProperty("USER", user);
            properties.setProperty("PASSWORD", password);
            properties.setProperty("JDBC_DRIVER", "org.postgresql.Driver");
            properties.store(new FileOutputStream(PROPERTIES_FILE), "Database configuration");
            System.out.println("Configuration updated successfully.");
            DBConnectionSingleton.restartConnection();
            System.out.println("Login successful!");
            return SUCCESS;
        } catch (Exception e) {
            System.err.println("Invalid Credentials or Database not found.");
            System.err.println(e.getMessage());
            return 1;
        }
    }
}
