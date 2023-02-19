package subcommands.catalog;

import picocli.CommandLine;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@CommandLine.Command(name = "remove", mixinStandardHelpOptions = true, version = "remove 0.1",
        description = "Remove a course from the catalog.")
public class Remove implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Course Code", paramLabel = "courseCode", descriptionKey = "courseCode")
    String courseCode;

    @Override
    public Integer call() {
        models.Catalog catalog;
        try {
            catalog = models.Catalog.retrieve(courseCode);
            if (catalog == null) {
                System.err.println("Course does not exist in the catalog.");
                return NOT_EXISTS;
            }
            catalog.delete();
            System.out.println("Course removed from the catalog.");
        } catch (SQLException e) {
            return database.access.Exception.handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred while removing the course from the catalog.");
            return UNKNOWN;
        }
        return SUCCESS;
    }
}
