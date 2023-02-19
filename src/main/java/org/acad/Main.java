package org.acad;

import database.access.DBConnectionSingleton;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.Callable;

@Command(name = "acad", mixinStandardHelpOptions = true, subcommands = {Catalog.class, Configuration.class, Calender.class, Curriculum.class}, version = "acad 0.1",
        description = "A command line interface for the Academic database.")
public class Main implements Callable<Integer> {
    public static void checkConfigFile() {
        try {
            String PropertiesFile = String.join(System.getProperty("file.separator"), System.getProperty("user.home"), ".academic", "database.properties");
            File file = new File(PropertiesFile);
            if (!file.exists()) {
                ClassLoader classLoader = DBConnectionSingleton.class.getClassLoader();
                file.getParentFile().mkdir();
                InputStream inputStream = classLoader.getResourceAsStream("database.properties");
                assert inputStream != null;
                Files.copy(inputStream, file.toPath());
            }
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) throws IOException {
        checkConfigFile();
        int exitCode = new CommandLine(new Main()).execute(args);
        if (exitCode == 0) {
            System.exit(0);
        }
        System.exit(1);
    }

    @Override
    public Integer call() {
        System.out.println("Use --help for more information.");
        return 0;
    }

}