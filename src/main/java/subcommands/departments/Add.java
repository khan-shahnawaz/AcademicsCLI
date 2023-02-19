package subcommands.departments;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.util.concurrent.Callable;
import static database.access.Exception.SUCCESS;
import static database.access.Exception.handleSQLException;

@Command(name = "add", mixinStandardHelpOptions = true, version = "departments 0.1",
        description = "Adds a department to the database.")
public class Add implements Callable<Integer> {
    @Option(names = {"-c", "--code"}, description = "Department Code.", required = true,interactive = true ,echo = true, prompt = "Department Code: ", arity = "0..1")
    private String code;

    @Option(names = {"-n", "--name"}, description = "Department Name.", required = true, interactive = true, echo = true, prompt = "Department Name: ", arity = "0..1")
    private String name;

    @Override
    public Integer call() throws Exception {
        models.Department department = new models.Department();
        department.setId(code);
        department.setName(name);
        String exitCode = department.save().toLowerCase();
        if (!exitCode.equals("00000")) {
            System.err.println("An error occurred while adding the department to the database.");
            return handleSQLException(exitCode, "Insertion failed.");
        }
        System.out.print("Department added successfully.");
        return SUCCESS;
    }
}
