package subcommands.offerings;

import picocli.CommandLine;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@CommandLine.Command(name = "instructor", description = "Add/Remove instructor for an offering")
public class Teacher implements Callable<Integer> {
    @CommandLine.Option(names = {"-r", "--remove"}, description = "Remove an instructor from an offering")
    boolean remove;
    @CommandLine.Parameters(index = "0", description = "Offering Id", paramLabel = "courseCode", descriptionKey = "courseCode")
    int offeringId;
    @CommandLine.Parameters(index = "1", description = "Instructor Email", paramLabel = "instructorId", descriptionKey = "instructorId")
    String instructorEmail;

    @Override
    public Integer call() {
        try {
            if (remove) {
                models.TeachingTeam teachingTeam = models.TeachingTeam.retrieve(offeringId, instructorEmail);
                String exitCode = teachingTeam.delete();
                if (!exitCode.equals("00000")) {
                    System.err.println("An error occurred while removing an instructor from the offering.");
                    return handleSQLException(exitCode, "Operation failed.");
                }
                System.out.println("Instructor removed from the offering.");
                return SUCCESS;
            }
            models.TeachingTeam teachingTeam = new models.TeachingTeam();
            teachingTeam.setInstructor(instructorEmail);
            teachingTeam.setCoordinator(false);
            teachingTeam.setId(offeringId);
            String exitCode = teachingTeam.save();
            if (!exitCode.equals("00000")) {
                System.err.println("An error occurred while adding an instructor to the offering.");
                return handleSQLException(exitCode, "Operation failed.");
            }
            System.out.println("Instructor added to the offering.");
            return SUCCESS;
        } catch (SQLException e) {
            return handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred while removing a prerequisite to the Offering.");
            return UNKNOWN;
        }
    }
}
