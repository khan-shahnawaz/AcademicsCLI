package subcommands.catalog;

import picocli.CommandLine;

import java.sql.SQLException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "remove", mixinStandardHelpOptions = true, version = "remove 0.1",
        description = "Remove a course from the catalog.")
public class Remove implements Callable<Integer> {
    public static int SUCCESS = 0;
    public static int ALREADY_EXISTS = 1;
    public static int UNAUTHORISED = 2;
    public static int UNKNOWN = 3;
    public static int NOT_EXISTS = 4;
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    @CommandLine.Parameters(index = "0", description = "Course Code", paramLabel = "courseCode", descriptionKey = "courseCode")
    String courseCode;

    @Override
    public Integer call() throws Exception {
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
            if (e.getSQLState().equalsIgnoreCase("42501")) {
                System.err.println("You do not have the required permissions to remove a course from the catalog.");
                return UNAUTHORISED;
            }
            System.err.println("An error occurred while removing the course from the catalog.");
            return UNKNOWN;
        } catch (Exception e) {
            System.err.println("An error occurred while removing the course from the catalog.");
            return UNKNOWN;
        }
        return SUCCESS;
    }
}
