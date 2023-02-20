package subcommands.offerings;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@Command(name = "prereq", mixinStandardHelpOptions = true, version = "prerequisite 0.1",
        description = "Add a prerequisite to a course in the offering.")
public class Prerequisite implements Callable<Integer> {
    @CommandLine.Option(names = {"-r", "--remove"}, description = "Remove a prerequisite from a course in the Offering.")
    boolean remove;
    @Parameters(index = "0", description = "Offering Id", paramLabel = "courseCode", descriptionKey = "courseCode")
    int offeringId;
    @Parameters(index = "1", description = "Prerequisite Course Code", paramLabel = "prerequisiteCourseCode", descriptionKey = "prerequisiteCourseCode")
    String prerequisiteCourseCode;
    @Parameters(index = "2", description = "Minimum Grade", paramLabel = "MinimumGrade", descriptionKey = "Minimum Grade", defaultValue = "E")
    String grade;

    public Integer call() {
        try {
            if (remove) {
                models.Prerequisite prerequisite = models.Prerequisite.retrieve(offeringId, prerequisiteCourseCode);
                if (prerequisite == null) {
                    System.err.println("Prerequisite does not exist in the offering.");
                    return NOT_EXISTS;
                }
                String exitCode = prerequisite.delete();
                if (!exitCode.equals("00000")) {
                    System.err.println("An error occurred while removing a prerequisite from the offering.");
                    return handleSQLException(exitCode, "Operation failed.");
                }
                System.out.println("Prerequisite removed from the offering.");
                return SUCCESS;
            }
            models.Prerequisite prerequisite = new models.Prerequisite();
            prerequisite.setMinGrade(grade);
            prerequisite.setId(offeringId);
            prerequisite.setPrerequisiteCode(prerequisiteCourseCode);
            String exitCode = prerequisite.save();
            if (!exitCode.equals("00000")) {
                System.err.println("An error occurred while adding a prerequisite to the offering.");
                return handleSQLException(exitCode, "Operation failed.");
            }
            System.out.println("Prerequisite added to the offering.");
            return SUCCESS;

        } catch (SQLException e) {
            return handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred while removing a prerequisite to the Offering.");
            return UNKNOWN;
        }
    }
}
