package subcommands.instructors;
import com.opencsv.CSVReaderHeaderAware;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import database.access.DBConnectionSingleton;
import static database.access.Exception.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.concurrent.Callable;
import static database.access.Exception.SUCCESS;
import static database.access.Exception.*;
import database.access.Transaction;

@Command(name = "remove", mixinStandardHelpOptions = true, version = "add 0.1",
        description = "Remove instructor from the database.")
public class Remove implements Callable<Integer> {
    @Option(names = {"-F", "--file"}, description = "CSV file containing instructor details.(Name,Department,Address,Phone,Email,Password)", defaultValue = "", interactive = true, echo = true, prompt = "CSV file Location: ", arity = "0..1")
    private String file;
    @Option(names = {"-e", "--email"}, description = "Email of the instructor.", defaultValue = "", interactive = true, echo = true, prompt = "Email: ", arity = "0..1")
    private String email;
    Connection connection = DBConnectionSingleton.getConnection();

    private Integer removeInstructor() throws Exception {
        models.Instructor instructor = models.Instructor.retrieve(email);
        if (instructor == null) {
            System.err.println("Instructor not found.");
            return NOT_EXISTS;
        }
        String exitCode = instructor.delete().toLowerCase();
        if (!exitCode.equals("00000")) {
            Transaction.rollback();
            System.err.println("An error occurred while removing the instructor from the database.");
            return handleSQLException(exitCode, "Deletion failed.");
        }
        Statement statement = connection.createStatement();
        String sql = String.format("DROP USER \"%s\"", email);
        statement.execute(sql);
        System.out.println("Instructor removed successfully.");
        return SUCCESS;
    }

    @Override
    public Integer call() {
        try {
            if (file.equals("")) {
                Transaction.start();
                int exitCode = removeInstructor();
                if (exitCode==SUCCESS) {
                    Transaction.commit();
                } else {
                    Transaction.rollback();
                }
                return exitCode;
            }
            Transaction.start();
            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new java.io.FileReader(file));
            java.util.Map<String, String> record;
            while ((record = reader.readMap()) != null) {
                email = record.get("Email");
                Integer exitCode = removeInstructor();
                if (exitCode != SUCCESS) {
                    return exitCode;
                }
            }
            Transaction.commit();
            return SUCCESS;
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}