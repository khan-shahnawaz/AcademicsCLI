package subcommands.departments;
import models.Department;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import static database.access.Exception.*;

@Command(name = "remove", mixinStandardHelpOptions = true, version = "departments 0.1",
        description = "Removes a department from the database.")
public class Remove implements Callable<Integer> {
    @Option(names = {"-c", "--code"}, description = "Department Code.", required = true, interactive = true, echo = true, prompt = "Department Code: ", arity = "0..1")
    private String code;

    public Integer call() throws Exception {
        try {
            models.Department department = Department.retrieve(code);
            if (department == null) {
                System.err.println("No such department exists.");
                return NOT_EXISTS;
            }
            String exitCode = department.delete().toLowerCase();
            if (!exitCode.equals("00000")) {
                System.err.println("An error occurred while removing the department from the database.");
                return handleSQLException(exitCode, "Deletion failed.");
            }
            System.out.print("Department removed successfully.");
            return SUCCESS;
        } catch (SQLException e) {
            return handleSQLException(e.getSQLState(), e.getMessage());
        }
    }
}
