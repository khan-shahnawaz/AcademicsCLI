package subcommands.offerings;

import picocli.CommandLine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@CommandLine.Command(name = "update", mixinStandardHelpOptions = true, description = "Update an offering in the database")
public class Update implements Callable<Integer> {
    @CommandLine.Option(names = {"-i", "--id"}, description = "Offering Id", paramLabel = "Offering Id", descriptionKey = "Offering Id")
    int offeringId;
    @CommandLine.Option(names = {"-g", "--CGPA"}, description = "Minimum CGPA", paramLabel = "CGPA", descriptionKey = "CGPA", defaultValue = "-1")
    float minCGPA;
    @CommandLine.Option(names = {"-s", "--status"}, description = "Offering Status", paramLabel = "status", descriptionKey = "status", interactive = true, echo = true, prompt = "Current Status: ", defaultValue = "", arity = "0..1")
    String status;

    @Override
    public Integer call() {
        try {
            ArrayList<models.Offering> offerings = models.Offering.retrieveAll();
            models.Offering offering = null;
            for (models.Offering o : offerings) {
                if (o.getId() == offeringId) {
                    offering = o;
                    break;
                }
            }
            if (minCGPA != -1) {
                offering.setMinCGPA(minCGPA);
            }
            if (!status.equals("")) {
                offering.setStatus(status);
            }
            String exitCode = offering.save();
            if (!exitCode.equals("00000")) {
                System.err.println("An error occurred while updating the offering.");
                return handleSQLException(exitCode, "Operation failed.");
            }
            System.out.println("Offering updated.");
            return SUCCESS;
        } catch (SQLException e) {
            return handleSQLException(e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred while removing a prerequisite to the Offering.");
            return UNKNOWN;
        }
    }
}